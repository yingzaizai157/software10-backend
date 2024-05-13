package com.cqupt.software_10.mapper.tasks;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.tasks.MyTask;
import com.cqupt.software_10.vo.DateCount;
import com.cqupt.software_10.vo.DateModelCount;
import org.apache.ibatis.annotations.Mapper;
import java.sql.Date;

import java.util.List;
import java.util.Map;

@Mapper
public interface MyTaskMapper extends BaseMapper<MyTask> {
    MyTask getlistbyId(Integer id);

    String getFeatureByTasknameAndModelname(String taskname, String modelname);

    void deleteTaskById(Integer id);

    List<MyTask> getTaskList();

    List<DateCount> getTaskNearlySevenDays();

    List<DateModelCount> getEveryTaskNearlySevenDays();

    List<MyTask> getlistbyTaskname(String taskname);
}
