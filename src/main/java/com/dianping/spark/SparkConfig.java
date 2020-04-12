package com.dianping.spark;

public class SparkConfig {


    //windows先需要配置hadoop的HADOOP_HOME https://github.com/4ttty/winutils

    public static String Data_Path = "file:///D:/IdeaProjects/dianping/src/main/resources/data/";

    public static String Behavior_File = Data_Path + "behavior.csv";

    public static String Feature_File = Data_Path + "feature.csv";

    public static String Alsmodel_Path = Data_Path + "alsmodel";

    public static String Lrmodel_Path = Data_Path + "lrmodel";

    public static String DB_URL = "jdbc:mysql://192.168.99.100:3306/dianping?user=root&password=123456&useUnicode=true&characterEncoding=UTF-8&useSSL=false";


}


