package com.cqupt.software_10.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.software_10.mapper.user.UserMapper;
import com.cqupt.software_10.service.user.UserService;
import com.cqupt.software_10.entity.user.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public PageInfo<User> findByPageService(Integer pageNum, Integer pageSize, QueryWrapper<User> queryWrapper) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> userInfos = userMapper.selectList(queryWrapper);
        return new PageInfo<>(userInfos);
    }

    @Override
    public User getUserByUserName(String username) {
        User user = userMapper.queryByUername(username);
        return user;
    }
}
