package com.cqupt.software_10.entity.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RuntimeBusCreateRequest {
    // private String icdCode;
    // private Integer algorithmId;
    //  private int busId;
    //  private String busName;
    //  private String busDescription;
    //  private String label;
    //  private int tableId;
    //  private int factorCount;
    private String symptom1;
    private String symptom2;
    private String symptom3;
    private String symptom4;
    private String symptom5;
}
