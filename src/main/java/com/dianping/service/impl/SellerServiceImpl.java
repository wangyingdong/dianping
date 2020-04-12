package com.dianping.service.impl;

import com.dianping.model.Seller;
import com.dianping.service.SellerService;
import com.dianping.common.exception.BusinessException;
import com.dianping.common.exception.Errors;
import com.dianping.mapper.SellerMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Resource
    private SellerMapper sellerMapper;

    @Override
    public Seller create(Seller sellerModel) {
        sellerModel.setCreatedAt(LocalDateTime.now());
        sellerModel.setUpdatedAt(LocalDateTime.now());
        sellerModel.setRemarkScore(new BigDecimal(0));
        sellerModel.setDisabledFlag(0);
        sellerMapper.insertSelective(sellerModel);
        return sellerModel;
    }

    @Override
    public Seller get(Integer id) {
        return sellerMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Seller> selectAll() {
        return sellerMapper.selectAll();
    }

    @Override
    public Seller changeStatus(Integer id, Integer disabledFlag) throws BusinessException {
        Seller sellerModel = get(id);
        if(sellerModel == null){
            throw new BusinessException(Errors.NO_OBJECT_FOUND_ERROR);
        }
        sellerModel.setDisabledFlag(disabledFlag);
        sellerMapper.updateByPrimaryKeySelective(sellerModel);
        return sellerModel;
    }

    @Override
    public Integer countAllSeller() {
        Example example = new Example(Seller.class);
        return sellerMapper.selectCountByExample(example);
    }
}
