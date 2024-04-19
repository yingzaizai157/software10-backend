package com.cqupt.software_10.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.entity.user.User;
import com.github.pagehelper.PageInfo;

public interface UserService extends IService<User> {
    User queryByUername(String username);

    //    下面方法是管理员端-数据管理新增
    void addTableSize(String uid, float tableSize);
    void minusTableSize(String uid, float tableSize);

    PageInfo<User> findByPageService(Integer pageNum, Integer pageSize, QueryWrapper<User> queryWrapper);

    User getUserByUserName(String username);
}
