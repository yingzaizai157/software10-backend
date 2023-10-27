package com.cqupt.software_10.service;

import com.cqupt.software_10.entity.TenKaggleDiabetesReflect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TenKaggleDiabetesReflectService {
    List<String> getAllKaggleDiabetesReflectColumn();

    List<TenKaggleDiabetesReflect> getAllKaggleDiabetesReflectData( String model );

    String getKaggleDiabetesReflectCnByEn(String enName);

    void updateContributorByCnCol(String  model, String colName, double value);
}
