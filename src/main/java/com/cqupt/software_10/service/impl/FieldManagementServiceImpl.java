package com.cqupt.software_10.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.software_10.dao.FieldManagementMapper;
import com.cqupt.software_10.entity.FieldManagementEntity;
import com.cqupt.software_10.service.FieldManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO 公共模块新增类

@Service
public class FieldManagementServiceImpl extends ServiceImpl<FieldManagementMapper, FieldManagementEntity> implements FieldManagementService {

    @Autowired
    private FieldManagementMapper fieldManagementMapper;

    @Override
    public List<FieldManagementEntity> getFiledByDiseaseName(String diseaseName) {

        List<FieldManagementEntity> res = fieldManagementMapper.getFiledByDiseaseName(diseaseName);

        return res;
    }

    @Override
    public void updateFieldsByDiseaseName(String diseaseName, List<String> fields) {

        fieldManagementMapper.updateFieldsByDiseaseName(diseaseName, fields);
    }
}
