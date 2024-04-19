package com.cqupt.software_10.service.impl.model;

import com.cqupt.software_10.entity.model.FeaContribute;
import com.cqupt.software_10.mapper.model.FeaContributeMapper;
import com.cqupt.software_10.service.model.FeaContributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeaContributeServiceImpl implements FeaContributeService {
    @Autowired
    private FeaContributeMapper feaContributeMapper;

    @Override
    public List<FeaContribute> getByTableName(String tableName) {
        return feaContributeMapper.getByTableName(tableName);
    }
}
