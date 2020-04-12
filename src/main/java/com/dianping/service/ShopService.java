package com.dianping.service;

import com.dianping.common.exception.BusinessException;
import com.dianping.model.Shop;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ShopService {

    Shop create(Shop shopModel) throws BusinessException;

    Shop get(Integer id);

    List<Shop> selectAll();

    List<Map<String, Object>> searchGroupByTags(String keyword, Integer categoryId, String tags);

    List<Shop> recommend(BigDecimal longitude, BigDecimal latitude);


    Integer countAllShop();

    List<Shop> search(BigDecimal longitude, BigDecimal latitude,
                      String keyword, Integer orderby, Integer categoryId, String tags);

    Map<String, Object> searchEs(BigDecimal longitude, BigDecimal latitude,
                                 String keyword, Integer orderby, Integer categoryId, String tags) throws IOException;


}
