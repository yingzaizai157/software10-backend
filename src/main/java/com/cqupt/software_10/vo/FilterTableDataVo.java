package com.cqupt.software_10.vo;
import com.cqupt.software_10.entity.CategoryEntity;
import com.cqupt.software_10.vo.AddDataFormVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO 公共模块新增类

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterTableDataVo {
    private AddDataFormVo addDataForm;
    private CategoryEntity nodeData;
    private String nodeid;
    private String status;
}

