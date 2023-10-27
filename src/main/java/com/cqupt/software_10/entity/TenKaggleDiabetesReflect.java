package com.cqupt.software_10.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@TableName("kaggle_diabetes")
@Data
public class TenKaggleDiabetesReflect {

    private Integer id;

    private String enCol;

    private String cnCol;

    private double modelFeatruesContributions;

    private double updateFeatruesContributions;

    private double rlFeatruesContributions;
}
