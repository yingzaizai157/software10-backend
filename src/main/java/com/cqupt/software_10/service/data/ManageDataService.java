package com.cqupt.software_10.service.data;

import com.cqupt.software_10.entity.data.ManageData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManageDataService {
    List<ManageData> getAllDataSet();

    List<String> getDataSetDisease();

    void deleteDataSetsById(int id);

    List<ManageData> searchByDiseaseName(String diseaseName);

    void updateDatasetsInfo(ManageData manageData);

    void uploadData(ManageData manageData);

    List<String> getAllcolumns(String datasetName);
}
