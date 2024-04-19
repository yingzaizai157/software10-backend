package com.cqupt.software_10.service.impl.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.software_10.entity.user.NewUser;
import com.cqupt.software_10.mapper.user.NewUserMapper;
import com.cqupt.software_10.service.user.NewUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewUserServiceImpl extends ServiceImpl<NewUserMapper, NewUser>
        implements NewUserService {

    @Autowired
    NewUserMapper newUserMapper;


    @Override
    public NewUser queryByUername(String username) {
        NewUser user = newUserMapper.queryByUername(username);
        return user;
    }

    @Override
    public void addTableSize(String uid,float tableSize) {
        newUserMapper.addTableSize(uid, tableSize);
    }

    @Override
    public void minusTableSize(String uid, float tableSize) {

        newUserMapper.minusTableSize(uid, tableSize);
    }


}
