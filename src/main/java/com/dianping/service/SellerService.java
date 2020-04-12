package com.dianping.service;

import com.dianping.common.exception.BusinessException;
import com.dianping.model.Seller;

import java.util.List;

public interface SellerService {

    Seller create(Seller sellerModel);

    Seller get(Integer id);

    List<Seller> selectAll();

    Seller changeStatus(Integer id,Integer disabledFlag) throws BusinessException;

    Integer countAllSeller();

}
