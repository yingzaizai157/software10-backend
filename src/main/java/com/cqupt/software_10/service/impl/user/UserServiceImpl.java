package com.cqupt.software_10.service.impl.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.software_10.mapper.user.UserMapper;
import com.cqupt.software_10.service.user.UserService;
import com.cqupt.software_10.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    UserMapper userMapper;
    @Override
    public User queryByUername(String username) {
        User user = userMapper.queryByUername(username);
        return user;
    }

    //    下面方法是管理员端-数据管理新增
    @Override
    public void addTableSize(String uid,float tableSize) {
        userMapper.addTableSize(uid, tableSize);
    }

    @Override
    public void minusTableSize(String uid, float tableSize) {

        userMapper.minusTableSize(uid, tableSize);
    }
}
