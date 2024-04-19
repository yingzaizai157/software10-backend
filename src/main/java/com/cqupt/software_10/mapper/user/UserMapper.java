package com.cqupt.software_10.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("UPDATE software10.software10user SET upload_size = upload_size-#{size} WHERE uid = #{id}")
    int decUpdateUserColumnById(@Param("id") String id, @Param("size") Double size);

    @Update("UPDATE software10.software10user SET upload_size = upload_size+#{size} WHERE uid = #{id}")
    int recoveryUpdateUserColumnById(@Param("id") String id, @Param("size") Double size);

    User queryByUername(String username);

    //    下面方法是管理员端-数据管理新增
    User selectByUid(String uid);
    void addTableSize(String uid, float tableSize);
    void minusTableSize(String uid, float tableSize);
}
