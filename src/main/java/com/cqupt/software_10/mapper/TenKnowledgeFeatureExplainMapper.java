package com.cqupt.software_10.mapper;

import com.cqupt.software_10.entity.TenKnowledgeFeature;
import com.cqupt.software_10.entity.TenKnowledgeFeatureExplain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TenKnowledgeFeatureExplainMapper {
    List<TenKnowledgeFeatureExplain> getAllKnowledgeFeatureExplain();
    String getKnowledgeFeatureExplainByCn(@Param("cnName") String cnName);

}
