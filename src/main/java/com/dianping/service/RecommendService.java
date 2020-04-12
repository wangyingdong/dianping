package com.dianping.service;

import java.util.List;

public interface RecommendService {



    /**
     * 找回数据，根据userId召回shopIdList
     *
     * @param userId
     * @return shopIdList
     */
    List<Integer> recall(Integer userId);


}
