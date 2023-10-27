package com.cqupt.software_10.service.impl.knowledge;

import com.cqupt.software_10.entity.knowledge.Features;
import com.cqupt.software_10.entity.model.ModelInfo;
import com.cqupt.software_10.mapper.knowledge.FeaturesMapper;
import com.cqupt.software_10.mapper.model.ModelInfoMapper;
import com.cqupt.software_10.service.knowledge.FeaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeaturesServiceImpl implements FeaturesService {

    @Autowired
    private FeaturesMapper featuresMapper;

    @Override
    public List<Features> getAllFeaturesKnowledges() {
        List<Features> features = featuresMapper.getAllFeaturesKnowledges();
        return features;
    }

    @Override
    public List<String> getKnowledgeDisease() {
        List<String> disease = featuresMapper.getKnowledgeDisease();
        return disease;
    }

    @Override
    public List<String> getKnowledgeFactorCN() {
        List<String> factorCN = featuresMapper.getKnowledgeFactorCN();
        return factorCN;
    }

    @Override
    public void deleteKnowledgeById(int id) {
        featuresMapper.deleteKnowledgeById(id);
    }

    @Override
    public List<Features> searchByDiseaseName(String diseaseName, String risks) {
        List<Features> diseaseNames = featuresMapper.searchByDiseaseName(diseaseName, risks);
        return diseaseNames;
    }

    @Override
    public void updateKnowledgeInfo(Features knowledge) {
        featuresMapper.updateKnowledgeInfo(knowledge);
    }

    @Override
    public void addKnowledgeInfo(Features knowledge) {
        featuresMapper.addKnowledgeInfo(knowledge);
    }

    @Override
    public List<String> getAllFeaturesENName() {
        List<String> featuresENName = featuresMapper.getAllFeaturesENName();
        return featuresENName;
    }

    @Override
    public String getExceptionKnowledge(String disease, String field) {
        return featuresMapper.getExceptionKnowledge(disease, field);
    }

    @Override
    public void setDoctorRate(Features feature) {
        featuresMapper.setDoctorRate(feature);
    }
}
