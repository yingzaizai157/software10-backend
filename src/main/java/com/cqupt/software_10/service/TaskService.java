
package com.cqupt.software_10.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.entity.AlgorithmUsageDailyStats;
import com.cqupt.software_10.entity.Task;

import java.util.List;

public interface TaskService extends IService<Task> {
    List<AlgorithmUsageDailyStats> getAlgorithmUsageDailyStatsLast7Days();

    List<String> getAlgorithmName();

    List<Task> getTaskList();

    Task getlistbyId(Integer id);

    void deleteTask(int id);
}
