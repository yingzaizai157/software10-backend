package com.cqupt.software_10.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RuntimeTaskRequest {

    private String taskType;
    private Integer bizId;
    private List<String> args=null;
    private String pyPath;
    private String request;
}
