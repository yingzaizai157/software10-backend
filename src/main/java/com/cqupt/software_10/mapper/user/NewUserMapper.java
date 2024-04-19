package com.cqupt.software_10.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.user.NewUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewUserMapper extends BaseMapper<NewUser> {
    NewUser queryByUername(String username);

    NewUser selectByUid(String uid);
    void addTableSize(String uid, float tableSize);
    void minusTableSize(String uid, float tableSize);
}
