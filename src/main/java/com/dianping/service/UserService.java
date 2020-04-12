package com.dianping.service;

import com.dianping.common.exception.BusinessException;
import com.dianping.model.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {

    List<User> selectUser();

    User getUser(int id);

    User login(String telphone,String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException;

    User register(User registerUser) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    Integer countAllUser();


}
