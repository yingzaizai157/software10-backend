package com.cqupt.software_10.service.impl.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.software_10.entity.user.User;
import com.cqupt.software_10.mapper.user.UserMapper;
import com.cqupt.software_10.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author hp
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-09-07 14:34:01
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {


}




