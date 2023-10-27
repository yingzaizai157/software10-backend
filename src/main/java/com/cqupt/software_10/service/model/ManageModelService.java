package com.cqupt.software_10.service.model;

import com.cqupt.software_10.entity.data.ManageData;
import com.cqupt.software_10.entity.model.ManageModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManageModelService {
    List<ManageModel> getAllModels();

    List<String> getModelsDisease();

    void deleteModelById(@Param("id") int id);

    List<ManageModel> searchByDiseaseName(@Param("diseaseName") String diseaseName);

    void updateModelInfo(@Param("manageModel") ManageModel manageModel);

    void uploadModel(ManageModel manageModel);
}
