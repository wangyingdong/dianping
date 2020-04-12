package com.dianping.mapper;

import com.dianping.common.mapper.BasicMapper;
import com.dianping.model.Shop;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ShopMapper extends BasicMapper<Shop> {

    List<Shop> recommend(BigDecimal longitude, BigDecimal latitude);

    List<Shop> search(BigDecimal longitude, BigDecimal latitude,
                      String keyword, Integer orderby, Integer categoryId, String tags);

    List<Map<String, Object>> searchGroupByTags(String keyword, Integer categoryId, String tags);


    List<Map<String, Object>> buildESQuery(Integer sellerId, Integer categoryId, Integer shopId);

}
