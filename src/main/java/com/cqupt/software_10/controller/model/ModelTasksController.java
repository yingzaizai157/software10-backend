package com.cqupt.software_10.controller.model;


import com.cqupt.software_10.common.R;
import com.cqupt.software_10.entity.model.ModelTasks;
import com.cqupt.software_10.service.model.ModelTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ten/modelTasks")
public class ModelTasksController {

    @Autowired
    private ModelTasksService modelTasksService;

    @GetMapping("/all")
    public R<List<ModelTasks>> getAllTasks(){
        List<ModelTasks> tasks = modelTasksService.getAllTasks();
        return new R<>(200,"成功",tasks, tasks.size());
    }

    @GetMapping("/upload")
    public R<List<ModelTasks>> upload(
            @RequestParam("taskName") String taskName,
            @RequestParam("diseaseName") String diseaseName,
            @RequestParam("modelName") String modelName,
            @RequestParam("tableName") String tableName,
            @RequestParam("remarks") String remarks
    ){
        Integer isTrained = 0;
        String address = null;
        ModelTasks task = new ModelTasks(null,taskName, tableName, modelName, remarks, isTrained, diseaseName, address);
        modelTasksService.upload(task);
        List<ModelTasks> tasks = modelTasksService.getAllTasks();

        return new R<>(200,"成功",tasks, tasks.size());
    }

}
