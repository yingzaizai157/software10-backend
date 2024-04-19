package com.cqupt.software_10.service.impl.data;

import com.cqupt.software_10.entity.TenKaggleDiabetes;
import com.cqupt.software_10.entity.data.ManageData;
import com.cqupt.software_10.mapper.data.KaggleDiabetesDataMapper;
import com.cqupt.software_10.mapper.data.ManageDataMapper;
import com.cqupt.software_10.service.data.KaggleDiabetesService;
import com.cqupt.software_10.service.data.ManageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageDataServiceImpl implements ManageDataService {
    @Autowired
    private ManageDataMapper manageDataMapper;



    @Override
    public List<ManageData> getAllDataSet() {
        List<ManageData> dataSet = manageDataMapper.getAllDataSet();
        return dataSet;
    }

    @Override
    public List<String> getDataSetDisease() {
        List<String> diseases = manageDataMapper.getDataSetDisease();
        return diseases;
    }

    @Override
    public void deleteDataSetsById(int id) {
        manageDataMapper.deleteDataSetsById(id);
    }

    @Override
    public List<ManageData> searchByDiseaseName(String diseaseName) {
        List<ManageData> newDataSets = manageDataMapper.searchByDiseaseName(diseaseName);
        return newDataSets;
    }

    @Override
    public void updateDatasetsInfo(ManageData manageData) {
        manageDataMapper.updateDatasetsInfo(manageData);
    }

    @Override
    public void uploadData(ManageData manageData) {
        manageDataMapper.uploadData(manageData);
    }

    @Override
    public List<String> getAllcolumns(String datasetName) {
        return manageDataMapper.getAllcolumns(datasetName);
    }
}
