package com.cqupt.software_10.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.entity.CategoryEntity;
import com.cqupt.software_10.entity.NewCategoryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// TODO 公共模块新增类
public interface NewCategoryService extends IService<NewCategoryEntity> {
    void removeNode(@Param("id") String id);
    List<NewCategoryEntity> getLevel2Label();
    List<NewCategoryEntity> getLabelByPid(@Param("pid") String pid);

    void updateTableNameByTableId(@Param("tableid") String tableid, @Param("tableName") String tableName, @Param("tableStatus") String tableStatus);
}
