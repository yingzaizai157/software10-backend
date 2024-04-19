package com.cqupt.software_10.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName t_table_manager
 */
@TableName(value ="table_describe",schema = "software10")
@Data
public class AdminDataManage implements Serializable {
    private String id;

    private String tableId;

    private String tableName;

    private String createUser;

    private String createTime;

    private String classPath;

    private String uid;

    private String tableStatus;

    private float tableSize;

    private static final long serialVersionUID = 1L;
}