package com.cqupt.software_10.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResult {
    private String tableName;
    private List<String> tableHeaders;
    private List<DataTable> res;
    private Integer code;
    private Exception e;
}
