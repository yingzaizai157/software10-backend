package com.cqupt.software_10.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;
import java.util.Date;
import java.util.List;
@Mapper
public interface LogMapper extends BaseMapper<LogEntity> {
    List<LogEntity> getAllLogs();

    List<LogEntity> selectDataByName(String searchType, String name);
}
