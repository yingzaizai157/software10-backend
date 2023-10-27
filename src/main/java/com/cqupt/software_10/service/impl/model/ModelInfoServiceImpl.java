package com.cqupt.software_10.service.impl.model;

import com.cqupt.software_10.entity.model.ModelInfo;
import com.cqupt.software_10.mapper.model.ModelInfoMapper;
import com.cqupt.software_10.service.model.ModelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelInfoServiceImpl implements ModelInfoService {

    @Autowired
    private ModelInfoMapper modelInfoMapper;

    @Override
    public List<ModelInfo> getAllInfo() {
        List<ModelInfo> modelInfos = modelInfoMapper.getAllInfo();
        return modelInfos;
    }

    @Override
    public Integer getPerformanceByFactors(String table, String reward, String model) {
        return modelInfoMapper.getPerformanceByFactors(table, reward, model);
    }

    @Override
    public List<String> getTables() {
        return modelInfoMapper.getTables();
    }

    @Override
    public List<String> getRewards() {
        return modelInfoMapper.getRewards();
    }

    @Override
    public List<String> getModelName() {
        return modelInfoMapper.getModelName();
    }

}
