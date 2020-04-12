package com.dianping.vo;

import lombok.Data;

@Data
public class Page {

    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String orderBy ="id desc";
    private boolean count = true;

}
