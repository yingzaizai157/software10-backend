package com.cqupt.software_10.service.impl;

import com.cqupt.software_10.entity.TenKnowledgeFeature;
import com.cqupt.software_10.entity.TenKnowledgeFeatureExplain;
import com.cqupt.software_10.mapper.TenKnowledgeFeatureExplainMapper;
import com.cqupt.software_10.service.TenKnowledgeFeatureExplainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenKnowledgeFeatureExpalinServiceImpl implements TenKnowledgeFeatureExplainService {
    @Autowired
    private TenKnowledgeFeatureExplainMapper tenKnowledgeFeatureExplainMapper;

    public List<TenKnowledgeFeatureExplain> getAllKnowledgeFeatureExplain() {
        return tenKnowledgeFeatureExplainMapper.getAllKnowledgeFeatureExplain();
    }

    public String getKnowledgeFeatureExplainByCn(String cnName) {
        return tenKnowledgeFeatureExplainMapper.getKnowledgeFeatureExplainByCn(cnName);
    }
}
