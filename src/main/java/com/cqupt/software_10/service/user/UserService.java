package com.cqupt.software_10.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.entity.InsertUserVo;
import com.cqupt.software_10.entity.UserPwd;
import com.cqupt.software_10.entity.user.User;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    List<User> getAll();

    User getUserByName(String userName);

    User getUserById(String id);

    void saveUser(User user);

    Page<User> getAllUserInfo(int pageNum  , int pageSize);

    boolean updateStatusById(Integer uid, Integer role ,double uploadSize, String status);

    boolean removeUserById(Integer uid);

    boolean insertUser(InsertUserVo user);

    Map<String, Object> getUserPage(int pageNum, int pageSize);

    boolean updatePwd(UserPwd user);

    List<User> querUser();
    User queryByUername(String username);

    //    下面方法是管理员端-数据管理新增
    void addTableSize(String uid, float tableSize);
    void minusTableSize(String uid, float tableSize);

    PageInfo<User> findByPageService(Integer pageNum, Integer pageSize, QueryWrapper<User> queryWrapper);

    User getUserByUserName(String username);
}
