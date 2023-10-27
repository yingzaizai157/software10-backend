package com.cqupt.software_10.entity.knowledge;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value ="knowledge_features")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Features {
    private int id;
    private String diseaseName;
    private String knowledgeType;
    private String riskFactors;
    private String riskFactorsCn;
    private int isException;
    private double exceptionLow;
    private double exceptionUp;
    private String exceptionExplain;
    private String resource;
    private String address;
    private double modelRate;
    private double doctorRate;
    private String typeCol;
    private String tableName;

    public Features(
            int id, String diseaseName, String riskFactorsCn,
            int isException, double exceptionLow, double exceptionUp,
            String exceptionExplain, String resource, String address
    ) {
        this.id = id;
        this.diseaseName = diseaseName;
        this.riskFactorsCn = riskFactorsCn;
        this.isException = isException;
        this.exceptionLow = exceptionLow;
        this.exceptionUp = exceptionUp;
        this.exceptionExplain = exceptionExplain;
        this.resource = resource;
        this.address = address;
    }

    public Features(String diseaseName, String riskFactorsCn, int isException, double exceptionLow, double exceptionUp, String exceptionExplain, String resource, String address) {
        this.diseaseName = diseaseName;
        this.riskFactorsCn = riskFactorsCn;
        this.isException = isException;
        this.exceptionLow = exceptionLow;
        this.exceptionUp = exceptionUp;
        this.exceptionExplain = exceptionExplain;
        this.resource = resource;
        this.address = address;
    }
}
