package com.cqupt.software_10.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.software_10.entity.TenDiabetes;
import com.cqupt.software_10.mapper.TenDiabetesMapper;
import com.cqupt.software_10.service.TenDiabetesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenDiabetesServiceImpl extends ServiceImpl<TenDiabetesMapper, TenDiabetes>
        implements TenDiabetesService {
    @Autowired
    private TenDiabetesMapper tenDiabetesMapper;


    @Override
    public List<String> getRiskFactorByTableName(Integer limitFactor) {
        List<String> riskFactor = tenDiabetesMapper.getRiskFactorByTableName(limitFactor);

        return riskFactor;
    }

    @Override
    public List<String> getComplicationsByTableName(Integer limitComplications) {
        List<String> complications = tenDiabetesMapper.getComplicationsByTableName(limitComplications);

        return complications;
    }

    @Override
    public List<String> getBaseFactorByTableName(Integer limitComplications) {
        List<String> baseFactor = tenDiabetesMapper.getBaseFactorByTableName(limitComplications);

        return baseFactor;
    }

    @Override
    public List<String> getCheckFactorByTableName(Integer limitCheckFactor) {
        List<String> checkFactor = tenDiabetesMapper.getCheckFactorByTableName(limitCheckFactor);

        return checkFactor;
    }
}
