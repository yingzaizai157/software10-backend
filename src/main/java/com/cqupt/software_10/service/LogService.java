package com.cqupt.software_10.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.entity.CategoryEntity;
import com.cqupt.software_10.entity.LogEntity;
import com.mysql.cj.log.Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// TODO 公共模块新增类
public interface LogService extends IService<LogEntity> {
    List<LogEntity> getAllLogs();
    void insertLog(String uid, Integer role, String operation);

    List<LogEntity> selectDataByName(String searchType, String name);
}
