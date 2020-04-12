package com.dianping.service;

import com.dianping.common.exception.BusinessException;
import com.dianping.model.Category;

import java.util.List;

public interface CategoryService {

    Category create(Category categoryModel) throws BusinessException;

    Category get(Integer id);

    List<Category> selectAll();

    Integer countAllCategory();
}
