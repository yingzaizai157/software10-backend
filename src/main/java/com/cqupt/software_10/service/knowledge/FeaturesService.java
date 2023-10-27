package com.cqupt.software_10.service.knowledge;

import com.cqupt.software_10.entity.knowledge.Features;
import com.cqupt.software_10.entity.model.ModelInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FeaturesService {
    List<Features> getAllFeaturesKnowledges();

    List<String> getKnowledgeDisease();

    List<String> getKnowledgeFactorCN();

    void deleteKnowledgeById(int id);

    List<Features> searchByDiseaseName(String diseaseName, String risks);

    void updateKnowledgeInfo(Features knowledge);

    void addKnowledgeInfo(Features knowledge);


    List<String> getAllFeaturesENName();

    String getExceptionKnowledge(String disease, String field);

    void setDoctorRate(Features feature);

}
