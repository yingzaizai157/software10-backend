package com.cqupt.software_10.mapper;

import com.cqupt.software_10.entity.TenKaggleDiabetesReflect;
import com.cqupt.software_10.entity.TenKnowledgeFeature;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TenKnowledgeFeatureMapper {
    List<TenKnowledgeFeature> getAllKnowledgeFeature();

}
