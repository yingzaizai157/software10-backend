package com.cqupt.software_10.entity.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value ="ManageModel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageModel {
    private int id;
    private String modelName;
    private String disease;
    private String tableName;
    private String creator;
    private String remark;

    public ManageModel(String modelName, String disease, String tableName, String creator, String remark) {
        this.modelName = modelName;
        this.disease = disease;
        this.tableName = tableName;
        this.creator = creator;
        this.remark = remark;
    }
}

