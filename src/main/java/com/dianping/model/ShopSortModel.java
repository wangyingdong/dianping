package com.dianping.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShopSortModel {

    private Integer shopId;
    private double score;
}
