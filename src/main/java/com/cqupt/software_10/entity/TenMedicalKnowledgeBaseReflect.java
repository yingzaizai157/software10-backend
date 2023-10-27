package com.cqupt.software_10.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("Medical_knowledge_base_reflect")
@Data
public class TenMedicalKnowledgeBaseReflect {

    private Integer id;

    private String enCol;

    private String cnCol;
}
