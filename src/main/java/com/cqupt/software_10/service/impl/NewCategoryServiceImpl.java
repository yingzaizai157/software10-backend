package com.cqupt.software_10.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.software_10.dao.CategoryMapper;
import com.cqupt.software_10.dao.NewCategoryMapper;
import com.cqupt.software_10.entity.CategoryEntity;
import com.cqupt.software_10.entity.NewCategoryEntity;
import com.cqupt.software_10.service.CategoryService;
import com.cqupt.software_10.service.NewCategoryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO 公共模块新增类

@Service
public class NewCategoryServiceImpl extends ServiceImpl<NewCategoryMapper, NewCategoryEntity>
        implements NewCategoryService {

    @Autowired
    NewCategoryMapper newCategoryMapper;

    @Override
    public void removeNode(String id) {
        newCategoryMapper.removeNode(id);
    }

    @Override
    public List<NewCategoryEntity> getLevel2Label() {
        return newCategoryMapper.getLevel2Label();
    }

    @Override
    public List<NewCategoryEntity> getLabelByPid(String pid) {
        return newCategoryMapper.getLabelByPid(pid);
    }
    public void updateTableNameByTableId(String tableid, String tableName, String tableStatus) {
        System.out.println("status: " + tableStatus);
        newCategoryMapper.updateTableNameByTableId(tableid, tableName, tableStatus);

    }
}
