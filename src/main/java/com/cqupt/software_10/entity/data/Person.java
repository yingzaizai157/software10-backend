package com.cqupt.software_10.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value ="person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private int id;
    private int pregnancies;
    private double glucose;
    private double bloodPressure;
    private double skinThickness;
    private double insulin;
    private double bmi;
    private double diabetesPedigreeFunction;
    private int age;
    private int user_id;

//    public Person(){    }

    public Person(int age, int pregnancies, double diabetesPedigreeFunction, double glucose, double bloodPressure,
                  double skinThickness, double insulin,double bmi){
        this.age = age;
        this.pregnancies = pregnancies;
        this.diabetesPedigreeFunction = diabetesPedigreeFunction;
        this.glucose = glucose;
        this.bloodPressure = bloodPressure;
        this.skinThickness = skinThickness;
        this.insulin = insulin;
        this.bmi = bmi;
    }
}