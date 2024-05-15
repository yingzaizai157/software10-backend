package com.cqupt.software_10.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@TableName(value ="filter_data_info",schema = "software10")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilterDataInfo {
    private Integer id;
    private String uid;
    private String username;
    private String createUser;
    private String cateId;
    private String parentId;
    private Timestamp filterTime;
}
