package com.cqupt.software_10.service.impl;

import com.cqupt.software_10.entity.TenKnowledgeFeature;
import com.cqupt.software_10.mapper.TenKnowledgeFeatureMapper;
import com.cqupt.software_10.service.TenKnowledgeFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenKnowledgeFeatureServiceImpl implements TenKnowledgeFeatureService {
    @Autowired
    private TenKnowledgeFeatureMapper tenKnowledgeFeatureMapper;

    public List<TenKnowledgeFeature> getAllKnowledgeFeature() {
        return tenKnowledgeFeatureMapper.getAllKnowledgeFeature();

    }
}
