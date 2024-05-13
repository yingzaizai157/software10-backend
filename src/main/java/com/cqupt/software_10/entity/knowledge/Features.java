package com.cqupt.software_10.entity.knowledge;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value ="knowledge",schema = "software10")
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

    public Features(
            int id, String diseaseName, String riskFactorsCn,
            int isException, double exceptionLow, double exceptionUp
    ) {
        this.id = id;
        this.diseaseName = diseaseName;
        this.riskFactorsCn = riskFactorsCn;
        this.isException = isException;
        this.exceptionLow = exceptionLow;
        this.exceptionUp = exceptionUp;
    }

    public Features(String diseaseName, String riskFactorsCn, int isException, double exceptionLow, double exceptionUp) {
        this.diseaseName = diseaseName;
        this.riskFactorsCn = riskFactorsCn;
        this.isException = isException;
        this.exceptionLow = exceptionLow;
        this.exceptionUp = exceptionUp;
    }
}
