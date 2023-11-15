package com.cqupt.software_10.entity.tasks;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value ="ModelTask")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelTask {
    private int id;
    private String taskName;
    private String diseaseName;
    private String tableName;
    private String modelName;
    private String remarks;
    private String isTrained;

    public ModelTask(String task_name, String diseaseName, String table_name, String model_name, String remarks, String is_trained) {
        this.taskName = task_name;
        this.diseaseName = diseaseName;
        this.tableName = table_name;
        this.modelName = model_name;
        this.remarks = remarks;
        this.isTrained = is_trained;
    }

    public ModelTask(int id, String taskName, String diseaseName, String tableName, String modelName, String remarks) {
        this.id = id;
        this.taskName = taskName;
        this.diseaseName = diseaseName;
        this.tableName = tableName;
        this.modelName = modelName;
        this.remarks = remarks;
    }
}
