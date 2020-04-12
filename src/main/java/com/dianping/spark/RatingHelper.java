package com.dianping.spark;

import lombok.Getter;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;


public class RatingHelper implements Serializable {

    public static Dataset<Row> getDataSetRating(SparkSession spark) {
        JavaRDD<String> csvFile = spark.read().textFile(SparkConfig.Behavior_File).toJavaRDD();
        JavaRDD<Rating> ratingJavaRDD = csvFile.map((Function<String, Rating>) v1 -> Rating.parseRating(v1));
        return spark.createDataFrame(ratingJavaRDD, Rating.class);
    }


    @Getter
    public static class Rating implements Serializable {
        private int userId;
        private int shopId;
        private int rating;

        //Parse String into Rating objects
        public static Rating parseRating(String str) {
            str = str.replace("\"", "");
            String[] strArr = str.split(",");
            int userId = Integer.parseInt(strArr[0]);
            int shopId = Integer.parseInt(strArr[1]);
            int rating = Integer.parseInt(strArr[2]);

            return new Rating(userId, shopId, rating);
        }

        public Rating(int userId, int shopId, int rating) {
            this.userId = userId;
            this.shopId = shopId;
            this.rating = rating;
        }
    }

}
