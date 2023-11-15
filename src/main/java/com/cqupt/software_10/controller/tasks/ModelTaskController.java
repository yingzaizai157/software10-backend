package com.cqupt.software_10.controller.tasks;

import com.cqupt.software_10.common.R;
import com.cqupt.software_10.entity.model.ManageModel;
import com.cqupt.software_10.entity.tasks.ModelTask;
import com.cqupt.software_10.service.tasks.ModelTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ten/tasks")
public class ModelTaskController {
    @Autowired
    private ModelTaskService modelTaskService;

    @GetMapping("/infos")
    public R<List<ModelTask>> getModelInfos(){
        List<ModelTask> modelTasks = modelTaskService.getAllModelTasks();
        return new R<>(200,"成功",modelTasks, modelTasks.size());
    }

    @GetMapping("/delete/{id}")
    public void deleteModelTask(@PathVariable("id") int id){
        modelTaskService.deleteModelTask(id);
    }

    @GetMapping("/update")
    public R<List<ModelTask>> updateSearchById(
            @RequestParam("id") int id,
            @RequestParam("taskName") String taskName,
            @RequestParam("diseaseName") String diseaseName,
            @RequestParam("tableName") String tableName,
            @RequestParam("modelName") String modelName,
            @RequestParam("remarks") String remarks
    ) {
        System.out.println("id: " + id + ", taskName: " + taskName + ", diseaseName: " + diseaseName +
                ", tableName: " + tableName + ", modelName: " + modelName + ", remarks: " + remarks);

        ModelTask modelTask = new ModelTask(id, taskName, diseaseName, tableName, modelName, remarks);
        modelTaskService.updateModelTask(modelTask);
        List<ModelTask> modelTasks = modelTaskService.getAllModelTasks();
        return new R<>(200,"成功",modelTasks, modelTasks.size());
    }
}