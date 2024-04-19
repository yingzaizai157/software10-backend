package com.cqupt.software_10.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// TODO 公共模块新增类

//@TableName("category")
@TableName(value ="new_category",schema = "software10")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewCategoryEntity {
    @TableId
    private String id;
    private Integer catLevel;
    private String label;
    private String parentId;
    private Integer isLeafs;
    private Integer isDelete;
    private String uid;
    private String status;
    private String username;

//    @TableField(exist = false)
//    private List<NewCategoryEntity> children;

}
