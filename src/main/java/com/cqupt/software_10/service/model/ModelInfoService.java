package com.cqupt.software_10.service.model;

import com.cqupt.software_10.entity.model.ModelInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ModelInfoService {

    List<ModelInfo> getAllInfo();

    Integer getPerformanceByFactors(String table, String reward, String model);

    List<String> getTables();

    List<String> getRewards();

    List<String> getModelName();
}
