package com.cqupt.software_10.service.impl;

import com.cqupt.software_10.entity.TenMedicalKnowledgeBase;
import com.cqupt.software_10.mapper.TenMedicalKnowledgeBaseMapper;
import com.cqupt.software_10.service.TenMedicalKnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenMedicalKnowledgeBaseServiceImpl implements TenMedicalKnowledgeBaseService {

    @Autowired
    private TenMedicalKnowledgeBaseMapper tenMedicalKnowledgeBaseMapper;

    @Override
    public List<TenMedicalKnowledgeBase> getAllMedicalKnowledge() {
        List<TenMedicalKnowledgeBase> allMedicalKnowledge = tenMedicalKnowledgeBaseMapper.getAllMedicalKnowledge();

        return allMedicalKnowledge;
    }
}
