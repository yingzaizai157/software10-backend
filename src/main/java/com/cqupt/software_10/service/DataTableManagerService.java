package com.cqupt.software_10.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.common.DataTable;

import java.util.List;

public interface DataTableManagerService extends IService<DataTable> {

    void updateDataTable(String table_name,String disease, String createName, int uid);

    List<DataTable> upalldata();

    void deletename(String tableName);


    List<String> upname();


    DataTable getTableInfo(String tableName);


}
