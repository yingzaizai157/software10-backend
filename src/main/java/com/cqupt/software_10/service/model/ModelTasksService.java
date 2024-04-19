package com.cqupt.software_10.service.model;

import com.cqupt.software_10.entity.model.ManageModel;
import com.cqupt.software_10.entity.model.ModelTasks;

import java.util.List;

public interface ModelTasksService {
    List<ModelTasks> getAllTasks();

    void upload(ModelTasks modelTasks);
}
