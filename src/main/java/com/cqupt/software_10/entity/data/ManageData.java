package com.cqupt.software_10.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value ="ManageData")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManageData {

    private int id;
    private String tableName;
    private String disease;
    private String creator;
    private String remark;

    public ManageData(String tableName, String disease, String creator, String remark) {
        this.tableName = tableName;
        this.disease = disease;
        this.creator = creator;
        this.remark = remark;
    }
}
