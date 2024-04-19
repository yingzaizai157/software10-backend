package com.cqupt.software_10.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="software10task",schema = "software10")
public class Task implements Serializable {

    private Integer id;
    private String taskname;
    private String leader;
    private String participant;
    private String disease;
    private String dataset;
    private String feature;
    private String targetcolumn;
    private String uid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createtime;
    private String tips;
    private String modelname;
    private String modeltype;
    private String modelparams;
    private String totallosses;
    private String precision;
    private String recall;
    private String fn;
    private String accuracy;
    private String fp;
    private String tn;
    private String totalrewards;
    private String f1;
    private String tp;
    private String avgshapvalue;

    public String getResult(){
         return toString();
    }



}
