package com.cqupt.software_10.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("kaggle_diabetes")
@Data
public class TenKaggleDiabetes {
    private Integer pregnancies;
    private Integer glucose;
    private Integer bloodPressure;
    private Integer skinThickness;
    private Integer insulin;
    private double bmi;
    private double diabetesPedigreeFunction;
    private Integer age;
    private Integer Outcome;
}
