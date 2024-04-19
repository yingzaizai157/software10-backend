package com.cqupt.software_10.service.model;

import com.cqupt.software_10.entity.model.FeaContribute;

import java.util.List;

public interface FeaContributeService {
    List<FeaContribute> getByTableName(String tableName);
}
