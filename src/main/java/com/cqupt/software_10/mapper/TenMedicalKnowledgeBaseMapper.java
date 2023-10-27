package com.cqupt.software_10.mapper;

import com.cqupt.software_10.entity.TenMedicalKnowledgeBase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TenMedicalKnowledgeBaseMapper {
    List<TenMedicalKnowledgeBase> getAllMedicalKnowledge();

    List<TenMedicalKnowledgeBase> getAllMedicalKnowledgeWithName();

}
