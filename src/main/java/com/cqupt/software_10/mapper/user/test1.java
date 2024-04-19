package com.cqupt.software_10.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.user.UserLog;
import com.cqupt.software_10.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface test1 extends BaseMapper<UserLog> {


    List<User> test1();
}
