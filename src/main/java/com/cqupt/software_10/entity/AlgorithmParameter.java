package com.cqupt.software_10.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmParameter {
    private String name;
    private Map<String, Object> forms;
}

