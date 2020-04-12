package com.dianping.service.impl;

import com.dianping.mapper.UserMapper;
import com.dianping.model.User;
import com.dianping.utils.EncodeUtils;
import com.dianping.common.exception.BusinessException;
import com.dianping.common.exception.Errors;
import com.dianping.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    public UserMapper userMapper;

    @Resource
    public EncodeUtils encodeUtils;

    @Override
    public List<User> selectUser() {
        return userMapper.selectAll();
    }

    @Override
    public User getUser(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User login(String telphone, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException {
        User userModel = userMapper.selectOne(User.builder().password(encodeUtils.encodeByMd5(password)).telphone(telphone).build());
        if(userModel == null){
            throw new BusinessException(Errors.NO_OBJECT_FOUND_ERROR);
        }
        return userModel;
    }

    @Override
    @Transactional
    public User register(User registerUser) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        registerUser.setPassword(encodeUtils.encodeByMd5(registerUser.getPassword()));
        registerUser.setCreateAt(LocalDateTime.now());
        registerUser.setUpdateAd(LocalDateTime.now());
        try {
            userMapper.insertSelective(registerUser);
        }catch (DuplicateKeyException e){
            throw new BusinessException(Errors.DATA_DUPLICATE_ERROR);
        }
        return registerUser;
    }

    @Override
    public Integer countAllUser() {
        Example example = new Example(User.class);
        return userMapper.selectCountByExample(example);
    }
}
