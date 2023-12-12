package com.cqupt.software_10.service.tasks;

import com.cqupt.software_10.entity.tasks.ModelTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModelTaskService  {

    List<ModelTask> getAllModelTasks();
    void deleteModelTask(int id);
    void updateModelTask(ModelTask manageModel);

}