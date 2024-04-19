package com.cqupt.software_10.mapper.tasks;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.tasks.MyTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyTaskMapper extends BaseMapper<MyTask> {
    MyTask getlistbyId(Integer id);

    String getFeatureByTasknameAndModelname(String taskname, String modelname);
}
