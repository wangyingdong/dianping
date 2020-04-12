package com.dianping.controller;

import com.dianping.common.exception.BusinessException;
import com.dianping.common.exception.Errors;
import com.dianping.model.Category;
import com.dianping.model.Shop;
import com.dianping.service.CategoryService;
import com.dianping.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/shop")
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;

    //推荐服务V1.0
    @RequestMapping("/recommend")
    @ResponseBody
    public List<Shop> recommend(@RequestParam(name = "longitude") BigDecimal longitude,
                                @RequestParam(name = "latitude") BigDecimal latitude) throws BusinessException {
        if (longitude == null || latitude == null) {
            throw new BusinessException(Errors.PARAMETER_ERROR);
        }

        List<Shop> shopModelList = shopService.recommend(longitude, latitude);
        return shopModelList;
    }


    //搜索服务V1.0
    @RequestMapping("/search")
    @ResponseBody
    public Map<String, Object> search(@RequestParam(name = "longitude") BigDecimal longitude,
                                      @RequestParam(name = "latitude") BigDecimal latitude,
                                      @RequestParam(name = "keyword") String keyword,
                                      @RequestParam(name = "orderby", required = false) Integer orderby,
                                      @RequestParam(name = "categoryId", required = false) Integer categoryId,
                                      @RequestParam(name = "tags", required = false) String tags) throws BusinessException, IOException {
        if (StringUtils.isEmpty(keyword) || longitude == null || latitude == null) {
            throw new BusinessException(Errors.PARAMETER_ERROR);
        }

        //List<Shop> shopModelList = shopService.search(longitude,latitude,keyword,orderby,categoryId,tags);
        Map<String, Object> result = shopService.searchEs(longitude, latitude, keyword, orderby, categoryId, tags);
        List<Shop> shopModelList = (List<Shop>) result.get("shop");

        List<Category> categoryModelList = categoryService.selectAll();

        //List<Map<String,Object>> tagsAggregation = shopService.searchGroupByTags(keyword,categoryId,tags);
        List<Map<String, Object>> tagsAggregation = (List<Map<String, Object>>) result.get("tags");

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("shop", shopModelList);
        resMap.put("category", categoryModelList);
        resMap.put("tags", tagsAggregation);
        return resMap;

    }


}
