package com.dianping.service.impl;

import com.dianping.mapper.RecommendMapper;
import com.dianping.model.Recommend;
import com.dianping.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private RecommendMapper recommendMapper;


    @Override
    public List<Integer> recall(Integer userId) {
        Recommend recommend = recommendMapper.selectByPrimaryKey(userId);
        if (recommend == null) {
            recommend = recommendMapper.selectByPrimaryKey(-1);
        }
        String[] shopIdArray = recommend.getRecommend().split(",");
        return Arrays.stream(shopIdArray).map(e -> Integer.parseInt(e)).collect(Collectors.toList());
    }
}
