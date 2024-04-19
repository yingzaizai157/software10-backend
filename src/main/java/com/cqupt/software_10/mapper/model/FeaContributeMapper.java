package com.cqupt.software_10.mapper.model;

import com.cqupt.software_10.entity.model.FeaContribute;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeaContributeMapper {
    List<FeaContribute> getByTableName(String tableName);
}
