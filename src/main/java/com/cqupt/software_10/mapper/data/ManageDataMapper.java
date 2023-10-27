package com.cqupt.software_10.mapper.data;

import com.cqupt.software_10.entity.TenKaggleDiabetes;
import com.cqupt.software_10.entity.data.ManageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ManageDataMapper {

    List<ManageData> getAllDataSet();

    List<String> getDataSetDisease();

    void deleteDataSetsById(@Param("id") int id);

    List<ManageData> searchByDiseaseName(@Param("diseaseName") String diseaseName);

    void updateDatasetsInfo(@Param("manageData") ManageData manageData);

    void uploadData(@Param("manageData") ManageData manageData);
}
