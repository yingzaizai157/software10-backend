package com.cqupt.software_10.mapper.model;

import com.cqupt.software_10.entity.TenKaggleDiabetes;
import com.cqupt.software_10.entity.data.ManageData;
import com.cqupt.software_10.entity.model.ManageModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ManageModelMapper {
    List<ManageModel> getAllModels();

    List<String> getModelsDisease();

    void deleteModelById(@Param("id") int id);

    List<ManageModel> searchByDiseaseName(@Param("diseaseName") String diseaseName);

    void updateModelInfo(@Param("manageModel") ManageModel manageModel);

    void uploadModel(@Param("manageModel") ManageModel manageModel);
}
