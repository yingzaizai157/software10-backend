package com.cqupt.software_10.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value ="software10user",schema = "software10")
@Data
public class User {

    @TableId(type = IdType.AUTO)
    @TableField("uid")
    private String uid;

    private String username;

    private String password;

    private String createTime;

    private String updateTime;

    private Integer role;

    // 新增字段

    @TableField(exist = false)
    private String code;

    private String userStatus;
    @TableField("answer_1")
    private String answer1;
    @TableField("answer_2")
    private String answer2;
    @TableField("answer_3")
    private String answer3;


    private double uploadSize;



    private static final long serialVersionUID = 1L;
}