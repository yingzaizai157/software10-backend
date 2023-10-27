package com.cqupt.software_10.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.entity.TenDiabetes;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TenDiabetesService extends IService<TenDiabetes> {

    List<String> getRiskFactorByTableName(Integer limitFactor);

    List<String> getComplicationsByTableName(Integer limitComplications);
    //
    List<String> getBaseFactorByTableName(Integer limitBaseFactor);

    List<String> getCheckFactorByTableName(Integer limitCheckFactor);
}
