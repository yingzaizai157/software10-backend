package com.cqupt.software_10.entity.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Params {
    public Integer reward;
    public Integer epoch;
    public Double gamma;
    public Double learning_rate;
    public String taskname;
    public String table_name;
    public List<String> cols;
    public List<String> labels;
}

