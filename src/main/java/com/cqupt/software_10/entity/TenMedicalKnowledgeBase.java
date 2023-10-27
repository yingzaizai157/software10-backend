package com.cqupt.software_10.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;

@TableName("Medical_knowledge_base")
@Data
public class TenMedicalKnowledgeBase {

    private Integer id;

    private String disasterName;

    private String disasterDesc;

    private String riskFactors;

    private String complications;

    private String knowledgeResources;

    private Date updateTime;

    private String remark;
}
