package com.cqupt.software_10.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.software_10.dao.ModelMapper;
import com.cqupt.software_10.entity.Model;
import com.cqupt.software_10.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model>
        implements ModelService {
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<String> getModelNames() {
        return modelMapper.getModelNames();
    }
}
