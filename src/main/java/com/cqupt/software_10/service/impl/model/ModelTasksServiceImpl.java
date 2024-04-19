package com.cqupt.software_10.service.impl.model;

import com.cqupt.software_10.entity.model.ModelTasks;
import com.cqupt.software_10.mapper.model.ModelTasksMapper;
import com.cqupt.software_10.service.model.ModelTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelTasksServiceImpl implements ModelTasksService {

    @Autowired
    private ModelTasksMapper modelTasksMapper;

    @Override
    public List<ModelTasks> getAllTasks() {
        return modelTasksMapper.getAllTasks();
    }

    @Override
    public void upload(ModelTasks modelTasks) {
        modelTasksMapper.upload(modelTasks);
    }
}
