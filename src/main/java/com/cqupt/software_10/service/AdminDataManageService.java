package com.cqupt.software_10.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.entity.AdminDataManage;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

// TODO 公共模块新增类
public interface AdminDataManageService extends IService<AdminDataManage> {
    List<String>  uploadDataTable(MultipartFile file, String pid, String tableName, String userName, String classPath, String uid, String tableStatus, float tableSize, String current_uid) throws IOException, ParseException;

    List<AdminDataManage> selectAllDataInfo();

    List<AdminDataManage> selectDataByName(String searchType, String name);
    AdminDataManage selectDataById(String id);
    List<String> selectParentIdsByTableId(String tableId);

    void deleteByTableName(String tablename);
    void deleteByTableId(String tableId);

    void updateById(String id, String tableName, String tableStatus);
    void updateDataBaseTableName(String old_name, String new_name);

    void updateInfo(String id, String tableid, String oldTableName, String tableName, String tableStatus, String current_uid);
}
