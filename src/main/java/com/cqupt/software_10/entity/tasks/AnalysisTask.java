package com.cqupt.software_10.entity.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisTask {
    public String taskName;
    public String leader;
    public String participant;
    public String disease;
    public String dataset;
    public List<String> feature;
    public String targetcolumn;
    public List<Model> models;
    public Integer uid;
}

