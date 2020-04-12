package com.dianping.mapper;

import com.dianping.common.mapper.BasicMapper;
import com.dianping.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BasicMapper<User> {
}