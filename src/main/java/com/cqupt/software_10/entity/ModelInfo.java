package com.cqupt.software_10.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="software10model",schema = "software10")
public class ModelInfo {
    private Integer id;
    private String modelname;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Timestamp createtime;
    private String publisher;
    private String defaultparams;
    private String remark;
    private String introduction;
    private String modelType;
    private Integer isSelect;


}
