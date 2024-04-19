package com.cqupt.software_10.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO 公共模块新增类

@TableName(value = "table_describe", schema = "software10")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TableDescribeEntity {
    @TableId
    private String id;
    private String tableId;
    private String tableName;
    private String createUser;
    private String createTime;
    private String classPath;
    private String uid;
    private String tableStatus;
    private Double tableSize;
    private static final long serialVersionUID = 1L;
}
