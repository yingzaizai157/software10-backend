package com.cqupt.software_10.service;

import com.cqupt.software_10.entity.TenKnowledgeFeature;
import com.cqupt.software_10.entity.TenKnowledgeFeatureExplain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TenKnowledgeFeatureExplainService {
    List<TenKnowledgeFeatureExplain> getAllKnowledgeFeatureExplain();
    String getKnowledgeFeatureExplainByCn(String cnName);
}
