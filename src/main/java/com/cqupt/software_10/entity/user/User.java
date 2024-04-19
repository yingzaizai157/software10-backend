package com.cqupt.software_10.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value ="software10user",schema = "software10")
@Data
public class User {

    @TableId
    private Integer uid;

    private String username;

    private String password;

    private Integer role;

    private Date createTime;

    private Date updateTime;
    private String userStatus;
    private String answer_1;
    private String answer_2;
    private String answer_3;
    private float uploadSize;

    private static final long serialVersionUID = 1L;
}