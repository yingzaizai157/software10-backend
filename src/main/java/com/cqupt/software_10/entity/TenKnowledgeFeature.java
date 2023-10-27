package com.cqupt.software_10.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TenKnowledgeFeature {
    private Integer id;
    private String riskFactors;
    private String riskFactorsCn;
    private Integer isException;
    private double exceptionLow;
    private double exceptionUp;
    private String exceptionExplain;
    private String resource;
    private double modelRate;
    private double doctorRate;
    private String typeCol;
    private String tableName;
}
