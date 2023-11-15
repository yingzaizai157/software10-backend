package com.cqupt.software_10.service.impl.tasks;

import com.cqupt.software_10.entity.model.ManageModel;
import com.cqupt.software_10.entity.tasks.ModelTask;
import com.cqupt.software_10.mapper.tasks.ModelTaskMapper;
import com.cqupt.software_10.service.tasks.ModelTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ModelTaskServiceImpl implements ModelTaskService {
    @Autowired
    private ModelTaskMapper modelTaskMapper;



    @Override
    public List<ModelTask> getAllModelTasks() {
        return modelTaskMapper.getAllModelTasks();
    }

    @Override
    public void deleteModelTask(int id) {
        modelTaskMapper.deleteModelById(id);
    }

    @Override
    public void updateModelTask(ModelTask modelTask) {
        modelTaskMapper.updateModelTask(modelTask);
    }
}