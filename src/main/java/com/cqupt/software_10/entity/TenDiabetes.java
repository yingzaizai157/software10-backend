package com.cqupt.software_10.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName(value ="Diabetes_dic")
@Data
public class TenDiabetes implements Serializable {
    private String name;

    private String mean;

    private String isMustTake;

    private String type;

    private String defaultValue;

    private String length;

    private String scope;

    private String content;

    private String extra;

    private Integer complications;

    private Integer baseRiskFactor;

    private Integer checkRiskFactor;

    private Integer riskContent;

    private static final long serialVersionUID = 1L;
}
