package com.cqupt.software_10.service.tasks;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.entity.tasks.MyTask;
import com.cqupt.software_10.vo.DateCount;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface MyTaskService extends IService<MyTask> {
    MyTask getlistbyId(Integer id);

    String getFeatureByTasknameAndModelname(String taskname, String modelname);

    void deleteTaskById(Integer id);

    List<MyTask> getTaskList();

    List<DateCount> getTaskNearlySevenDays();

}
