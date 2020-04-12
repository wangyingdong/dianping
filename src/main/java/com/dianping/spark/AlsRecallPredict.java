package com.dianping.spark;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


public class AlsRecallPredict implements Serializable {


    public static void main(String[] args) {
        //初始化spark运行环境
        SparkSession spark = SparkSession.builder().master("local").appName("DianpingApp").getOrCreate();

        //将模型加载到内存之中
        ALSModel alsModel = ALSModel.load(SparkConfig.Alsmodel_Path);

        Dataset<Row> rating = RatingHelper.getDataSetRating(spark);

        //给5个用户做离线的召回结果预测
        Dataset<Row> users = rating.select(alsModel.getUserCol()).distinct().limit(5);
        Dataset<Row> userRecs = alsModel.recommendForUserSubset(users, 20);
        userRecs.foreachPartition(new ForeachPartitionFunction<Row>() {

            @Override
            public void call(Iterator<Row> t) throws Exception {
                // A list of <userId> - <shopIdList> pairs
                List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
                t.forEachRemaining(action -> {
                    int userId = action.getInt(0);
                    List<GenericRowWithSchema> recommendationList = action.getList(1);
                    // get the recommended shopIdList for the user
                    List<Integer> shopIdList = new ArrayList<Integer>();
                    recommendationList.forEach(row -> {
                        Integer shopId = row.getInt(0);
                        shopIdList.add(shopId);
                    });
                    // convert shopIdList into String
                    String recommendData = StringUtils.join(shopIdList, ",");
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("userId", userId);
                    map.put("shopIdList", recommendData);
                    // add into result map
                    data.add(map);
                });
                saveRecommend(data);
            }

        });
    }


    private static void saveRecommend(List<Map<String, Object>> data) throws SQLException {
        Connection connection = DriverManager.getConnection(SparkConfig.DB_URL);

        // Parse data using dynamic binding
        PreparedStatement preparedStatement = connection.prepareStatement("insert into recommend(id,recommend)values(?,?)");
        data.forEach(stringObjectMap -> {
            try {
                preparedStatement.setInt(1, (Integer) stringObjectMap.get("userId"));
                preparedStatement.setString(2, (String) stringObjectMap.get("shopIdList"));
                preparedStatement.addBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        preparedStatement.executeBatch();
        connection.close();
    }
}