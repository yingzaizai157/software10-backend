package com.cqupt.software_10.service.impl.model;

import com.cqupt.software_10.entity.data.ManageData;
import com.cqupt.software_10.entity.model.ManageModel;
import com.cqupt.software_10.mapper.data.ManageDataMapper;
import com.cqupt.software_10.mapper.model.ManageModelMapper;
import com.cqupt.software_10.service.model.ManageModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageModelServiceImpl implements ManageModelService {
    @Autowired
    private ManageModelMapper manageModelMapper;

    @Override
    public List<ManageModel> getAllModels() {
        List<ManageModel> models = manageModelMapper.getAllModels();
        return models;
    }

    @Override
    public List<String> getModelsDisease() {
        List<String> diseases = manageModelMapper.getModelsDisease();
        return diseases;
    }

    @Override
    public void deleteModelById(int id) {
        manageModelMapper.deleteModelById(id);
    }

    @Override
    public List<ManageModel> searchByDiseaseName(String diseaseName) {
        List<ManageModel> newModels = manageModelMapper.searchByDiseaseName(diseaseName);
        return newModels;
    }

    @Override
    public void updateModelInfo(ManageModel manageModel) {
        manageModelMapper.updateModelInfo(manageModel);
    }

    @Override
    public void uploadModel(ManageModel manageModel) {
        manageModelMapper.uploadModel(manageModel);
    }
}
