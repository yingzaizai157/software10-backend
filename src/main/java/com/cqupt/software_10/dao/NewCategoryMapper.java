package com.cqupt.software_10.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.NewCategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// TODO 公共模块新增类

@Mapper
public interface NewCategoryMapper extends BaseMapper<NewCategoryEntity> {
    void removeNode(@Param("id") String id);
    List<NewCategoryEntity> getLevel2Label();
    List<NewCategoryEntity> getLabelByPid(@Param("pid") String pid);

    void updateTableNameByTableId(@Param("tableid") String tableid, @Param("tableName") String tableName, @Param("tableStatus") String tableStatus);
}
