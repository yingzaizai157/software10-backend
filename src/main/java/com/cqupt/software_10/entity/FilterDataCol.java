package com.cqupt.software_10.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value ="filter_data_col",schema = "software10")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilterDataCol {
    private Integer id;
    private Integer characterId;
    private String featureName;
    private String chName;
    private String computeOpt;
    private String unit;
    private String type;
    private String value;
    private Integer opt;
    private Integer filterDataInfoId;
    private String range;
}
