package com.cqupt.software_10.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value ="log",schema = "software10")
@Data
public class LogEntity {
    @TableId
    private Integer id;

    private String uid;
    private String username;
    private String operation;
    private String opTime;
    private Integer role;

    private static final long serialVersionUID = 1L;
}

