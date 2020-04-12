package com.dianping.service.impl;


import com.dianping.mapper.CategoryMapper;
import com.dianping.service.CategoryService;
import com.dianping.common.exception.BusinessException;
import com.dianping.common.exception.Errors;
import com.dianping.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public Category create(Category Category) throws BusinessException {
        Category.setCreatedAt(LocalDateTime.now());
        Category.setUpdatedAt(LocalDateTime.now());

        try {
            categoryMapper.insertSelective(Category);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(Errors.DATA_DUPLICATE_ERROR);
        }


        return get(Category.getId());
    }

    @Override
    public Category get(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Category> selectAll() {
        Example example = new Example(Category.class);
        example.setOrderByClause("sort desc,id asc");
        return categoryMapper.selectByExample(example);
    }

    @Override
    public Integer countAllCategory() {
        Example example = new Example(Category.class);
        return categoryMapper.selectCountByExample(example);
    }
}
