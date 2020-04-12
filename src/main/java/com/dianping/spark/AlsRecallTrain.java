package com.dianping.spark;

import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.io.Serializable;

//als召回算法的训练
public class AlsRecallTrain implements Serializable {

    public static void main(String[] args) throws IOException {
        SparkSession spark = SparkSession.builder().master("local").appName("DianPingApp").getOrCreate();
        Dataset<Row> rating = RatingHelper.getDataSetRating(spark);

        //将所有的rating数据分成82份
        Dataset<Row>[] splits = rating.randomSplit(new double[]{0.8, 0.2});

        Dataset<Row> trainingData = splits[0];
        Dataset<Row> testingData = splits[1];

        //过拟合：增大数据规模，减少rank，增大正则化的系数
        //欠拟合：增加rank,减少正则化系数
        ALS als = new ALS().setMaxIter(10).setRank(5).setRegParam(0.01)
                .setUserCol("userId").setItemCol("shopId").setRatingCol("rating");

        //模型训练
        ALSModel alsModel = als.fit(trainingData);

        //模型评测
        Dataset<Row> predictions = alsModel.transform(testingData);

        //rmse 均方根误差，预测值与真实值的偏差的平法除以观测次数，开个根号
        RegressionEvaluator evaluator = new RegressionEvaluator().setMetricName("rmse")
                .setLabelCol("rating")
                .setPredictionCol("prediction");

        double rmse = evaluator.evaluate(predictions);
        System.out.println("rmse=" + rmse);
        alsModel.save(SparkConfig.Alsmodel_Path);

    }


}