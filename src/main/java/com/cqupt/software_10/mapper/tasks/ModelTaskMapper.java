package com.cqupt.software_10.mapper.tasks;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.model.ManageModel;
import com.cqupt.software_10.entity.tasks.ModelTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface ModelTaskMapper extends BaseMapper<ModelTask> {

    List<ModelTask> getAllModelTasks();
    void deleteModelById(@Param("id") int id);

    void updateModelTask(@Param("modelTask") ModelTask modelTask);

}