package com.dianping.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.dianping.mapper.ShopMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CanalScheduling implements Runnable, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Resource
    private CanalConnector canalConnector;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private RestHighLevelClient highLevelClient;

    @Override
    @Scheduled(fixedDelay = 100)
    public void run() {
        long batchId = -1;
        int batchSize = 100;
        try {
            Message message = canalConnector.getWithoutAck(batchSize);
            batchId = message.getId();
            List<CanalEntry.Entry> entries = message.getEntries();
            if (batchId != -1 && entries.size() > 0) {
                entries.forEach(entry -> {
                    if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {
                        //解析处理
                        publishCanalEvent(entry);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void publishCanalEvent(CanalEntry.Entry entry) {
        CanalEntry.EventType eventType = entry.getHeader().getEventType();
        String database = entry.getHeader().getSchemaName();
        String table = entry.getHeader().getTableName();
        CanalEntry.RowChange change = null;

        try {
            change = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return;
        }

        change.getRowDatasList().forEach(rowData -> {
            List<CanalEntry.Column> columns = rowData.getAfterColumnsList();
            String primaryKey = "id";
            CanalEntry.Column idColumn = columns.stream().filter(column -> column.getIsKey() && primaryKey.equals(column.getName())).findFirst().orElse(null);
            Map<String, Object> dataMap = parseColumnsToMap(columns);
            indexES(dataMap, database, table);
        });


    }

    private void indexES(Map<String, Object> dataMap, String database, String table) {
        if (!StringUtils.equals("dianping", database)) {
            return;
        }
        List<Map<String, Object>> result;
        if (StringUtils.equals("seller", table)) {
            result = shopMapper.buildESQuery(new Integer((String) dataMap.get("id")), null, null);
        } else if (StringUtils.equals("category", table)) {
            result = shopMapper.buildESQuery(null, new Integer((String) dataMap.get("id")), null);
        } else if (StringUtils.equals("shop", table)) {
            result = shopMapper.buildESQuery(null, null, new Integer((String) dataMap.get("id")));
        } else {
            return;
        }

        for (Map<String, Object> map : result) {
            IndexRequest indexRequest = new IndexRequest("shop");
            indexRequest.id(String.valueOf(map.get("id")));
            indexRequest.source(map);
            try {
                highLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Map<String, Object> parseColumnsToMap(List<CanalEntry.Column> columns) {
        Map<String, Object> jsonMap = new HashMap<>();
        columns.forEach(column -> {
            if (column == null) {
                return;
            }
            jsonMap.put(column.getName(), column.getValue());
        });
        return jsonMap;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
