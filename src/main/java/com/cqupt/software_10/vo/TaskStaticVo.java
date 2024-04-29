package com.cqupt.software_10.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStaticVo {
    private List<String> date;
    private List<Integer> number;

}
