package com.cqupt.software_10.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.common.TableManagerDTO;
import com.cqupt.software_10.entity.TableManager;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author hp
* @description 针对表【t_table_manager】的数据库操作Mapper
* @createDate 2023-05-23 15:10:20
* @Entity generator.domain.TTableManager
*/

@Mapper
public interface TableManagerMapper extends BaseMapper<TableManager> {


    List<TableManager> getAllData();

    List<String> getFiledByTableName(String tableName);

    List<String> getCommentsByTableName(String tableName);

    List<Object> getInfoByTableName(String tableName);

    TableManager getInfoByFiled(String param);

    void insertTableManager(TableManagerDTO tableManagerDTO);
}




