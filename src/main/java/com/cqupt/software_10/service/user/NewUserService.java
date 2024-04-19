package com.cqupt.software_10.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.entity.user.NewUser;

public interface NewUserService extends IService<NewUser> {
    NewUser queryByUername(String username);
    void addTableSize(String uid, float tableSize);
    void minusTableSize(String uid, float tableSize);
}
