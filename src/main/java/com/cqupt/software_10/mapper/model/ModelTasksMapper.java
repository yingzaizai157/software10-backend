package com.cqupt.software_10.mapper.model;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.TenDiabetes;
import com.cqupt.software_10.entity.model.ModelTasks;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ModelTasksMapper {
    List<ModelTasks> getAllTasks();

    void upload(ModelTasks modelTasks);

//    Integer getPerformanceByFactors(@Param("table") String table, @Param("reward") String reward, @Param("model") String model);


//    List<String> getTables();
//
//    List<String> getRewards();
//
//    List<String> getModelName();
}
