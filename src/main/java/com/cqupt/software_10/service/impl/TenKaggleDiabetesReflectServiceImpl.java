package com.cqupt.software_10.service.impl;

import com.cqupt.software_10.entity.TenKaggleDiabetesReflect;
import com.cqupt.software_10.entity.TenMedicalKnowledgeBaseReflect;
import com.cqupt.software_10.entity.pojo.TwoStringDouble;
import com.cqupt.software_10.mapper.TenKaggleDiabetesReflectMapper;
import com.cqupt.software_10.service.TenKaggleDiabetesReflectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenKaggleDiabetesReflectServiceImpl implements TenKaggleDiabetesReflectService {

    @Autowired
    private TenKaggleDiabetesReflectMapper tenKaggleDiabetesReflectMapper;

    @Override
    public List<String> getAllKaggleDiabetesReflectColumn() {
        List<String> column = tenKaggleDiabetesReflectMapper.getKaggleDiabetesReflectColumn();

        return column;
    }

    @Override
    public List<TenKaggleDiabetesReflect> getAllKaggleDiabetesReflectData(String model) {
        List<TenKaggleDiabetesReflect> kaggleDiabetesReflects = tenKaggleDiabetesReflectMapper.getKaggleDiabetesReflect(model);
        return kaggleDiabetesReflects;
    }

    @Override
    public String getKaggleDiabetesReflectCnByEn(String enName) {
        String cnCol = tenKaggleDiabetesReflectMapper.getKaggleDiabetesReflectCnByEn(enName);
        return cnCol;
    }

    @Override
    public void updateContributorByCnCol(String model, String colName, double value) {
        TwoStringDouble twoStringDouble = new TwoStringDouble(model, colName, value);
        tenKaggleDiabetesReflectMapper.updateContributorByCnCol(twoStringDouble);
    }
}
