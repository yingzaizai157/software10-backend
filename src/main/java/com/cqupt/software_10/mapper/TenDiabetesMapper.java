package com.cqupt.software_10.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.TenDiabetes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TenDiabetesMapper extends BaseMapper<TenDiabetes> {
    List<String> getRiskFactorByTableName(@Param("limitFactor") Integer limitFactor);

    List<String> getComplicationsByTableName(@Param("limitComplications") Integer limitComplications);

    List<String> getBaseFactorByTableName(@Param("limitBaseFactor") Integer limitBaseFactor);

    List<String> getCheckFactorByTableName(@Param("limitCheckFactor") Integer limitCheckFactor);
}
