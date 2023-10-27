package com.cqupt.software_10.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwoStringDouble {
    private String model;
    private String colName;
    private double value;
}
