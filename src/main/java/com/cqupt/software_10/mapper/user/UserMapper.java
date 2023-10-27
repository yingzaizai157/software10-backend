package com.cqupt.software_10.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
* @author hp
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-09-07 14:34:01
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
     
}




