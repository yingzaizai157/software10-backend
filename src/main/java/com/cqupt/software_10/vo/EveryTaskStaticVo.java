package com.cqupt.software_10.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EveryTaskStaticVo {
    private List<String> date;
//    private List<Map<String,Integer>> tasksNumber;

    private List<Integer> dqn;
    private List<Integer> svm;
    private List<Integer> knn;
}
