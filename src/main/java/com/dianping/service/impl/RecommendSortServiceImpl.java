package com.dianping.service.impl;

import com.dianping.model.ShopSortModel;
import com.dianping.spark.SparkConfig;
import com.dianping.service.RecommendSortSerivce;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RecommendSortServiceImpl implements RecommendSortSerivce, Serializable {


    private SparkSession spark;

    private LogisticRegressionModel lrModel;


    @PostConstruct
    public void init() {
        //加载
        spark = SparkSession.builder().master("local").appName("DianpingApp").getOrCreate();
        lrModel = LogisticRegressionModel.load(SparkConfig.Lrmodel_Path);
    }


    @Override
    public List<Integer> sort(List<Integer> shopIdList, Integer userId) {
        //需要根据lrModel所需要的11维的X，生成特征，然后调用其预测方法
        List<ShopSortModel> list = new ArrayList<>();
        shopIdList.forEach(shopId -> {
            //TODO 这里是模拟的假数据，可以从数据库或缓存中拿到对应的用户，年龄，评分，价格等做特征转换生成feture向量
            Vector v = Vectors.dense(1, 0, 0, 0, 0, 1, 0.6, 0, 0, 1, 0);
            Vector result = lrModel.predictProbability(v);
            double[] arr = result.toArray();
            double score = arr[1];
            ShopSortModel shopSortModel = ShopSortModel.builder().shopId(shopId).score(score).build();
            list.add(shopSortModel);
        });

        List<ShopSortModel> sortList = list.stream()
                .sorted(Comparator.comparingDouble(ShopSortModel::getScore))
                .collect(Collectors.toList());

        return sortList.stream().map(s -> s.getShopId()).collect(Collectors.toList());
    }

}
