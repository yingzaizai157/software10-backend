package com.cqupt.software_10.entity.tasks;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Res {
    public String total_losses;
    public Double precision;
    public Double recall;
    public Integer FN;
    public Double accuracy;
    public Integer FP;
    public Integer TN;
    public String total_rewards;
    public Double f1;
    public Integer TP;
    public String avg_shapvalue;
}

