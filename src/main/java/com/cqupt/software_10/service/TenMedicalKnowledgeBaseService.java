package com.cqupt.software_10.service;

import com.cqupt.software_10.entity.TenMedicalKnowledgeBase;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TenMedicalKnowledgeBaseService {
    List<TenMedicalKnowledgeBase> getAllMedicalKnowledge();
}
