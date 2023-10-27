package com.cqupt.software_10.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.common.DataTable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DataTableManagerMapper extends BaseMapper<DataTable> {

    void insertDataTable(DataTable dataTable);

    List<DataTable> upalldata();

    void deletetablename(String tablename);

    List<String> upname();

}
