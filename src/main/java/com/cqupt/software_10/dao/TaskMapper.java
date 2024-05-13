package com.cqupt.software_10.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.AlgorithmUsageDailyStats;
import com.cqupt.software_10.entity.ModelInfo;
import com.cqupt.software_10.entity.Task;
import com.cqupt.software_10.entity.tasks.MyTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    List<AlgorithmUsageDailyStats> getAlgorithmUsageDailyStatsLast7Days();

    List<String> getAlgorithmName();

    List<Task> getTaskList();


    Task getlistbyId(Integer id);

    void deleteTask(int id);


    Map<String,List<MyTask>> GetAllTaskFrequency();

    List<ModelInfo> getModelInfo();
}