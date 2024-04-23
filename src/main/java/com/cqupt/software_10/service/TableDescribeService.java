package com.cqupt.software_10.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.entity.TableDescribeEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

// TODO 公共模块新增类
public interface TableDescribeService extends IService<TableDescribeEntity> {
    @Transactional
    List<String> uploadDataTable(MultipartFile file, String pid, String tableName, String userName, String classPath, String uid, String tableStatus, Double tableSize) throws IOException, ParseException;

    List<TableDescribeEntity> selectAllDataInfo();

    List<TableDescribeEntity> selectDataByName(String searchType, String name);
    TableDescribeEntity selectDataById(String id);

    void deleteByTableName(String tablename);
    void deleteByTableId(String tableId);

    void updateById(String id, String tableName, String tableStatus);
    void updateDataBaseTableName(String old_name, String new_name);

    void updateInfo(String id, String tableid, String oldTableName, String tableName, String tableStatus);


    Integer getTableColumnNum(String tableName);
    Integer getTableRowNum(String tableName);
}
