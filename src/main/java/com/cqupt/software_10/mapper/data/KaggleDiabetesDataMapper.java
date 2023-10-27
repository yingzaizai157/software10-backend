package com.cqupt.software_10.mapper.data;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.TenDiabetes;
import com.cqupt.software_10.entity.TenKaggleDiabetes;
import com.cqupt.software_10.entity.knowledge.Features;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface KaggleDiabetesDataMapper {

    List<TenKaggleDiabetes> getAllKaggleDiabetesData();
}
