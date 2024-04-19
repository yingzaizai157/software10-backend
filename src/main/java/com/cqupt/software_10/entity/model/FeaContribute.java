package com.cqupt.software_10.entity.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value ="table_cols_contribute")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeaContribute {
    private Integer id;
    private String tableName;
    private String enCol;
    private String cnCol;
    private Double modelFeatruesContributions;
    private Double updateFeatruesContributions;
    private Double rlFeatruesContributions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEnCol() {
        return enCol;
    }

    public void setEnCol(String enCol) {
        this.enCol = enCol;
    }

    public String getCnCol() {
        return cnCol;
    }

    public void setCnCol(String cnCol) {
        this.cnCol = cnCol;
    }

    public Double getModelFeatruesContributions() {
        return modelFeatruesContributions;
    }

    public void setModelFeatruesContributions(Double modelFeatruesContributions) {
        this.modelFeatruesContributions = modelFeatruesContributions;
    }

    public Double getUpdateFeatruesContributions() {
        return updateFeatruesContributions;
    }

    public void setUpdateFeatruesContributions(Double updateFeatruesContributions) {
        this.updateFeatruesContributions = updateFeatruesContributions;
    }

    public Double getRlFeatruesContributions() {
        return rlFeatruesContributions;
    }

    public void setRlFeatruesContributions(Double rlFeatruesContributions) {
        this.rlFeatruesContributions = rlFeatruesContributions;
    }
}
