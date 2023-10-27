package com.cqupt.software_10.entity.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value ="model_info")
@Data
public class ModelInfo {
    private int id;
    private String name;
    private String disease;
    private String dataTable;
    private String rewardType;
    private double performance;
}
