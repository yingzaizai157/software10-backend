package com.cqupt.software_10.service.impl;

import com.cqupt.software_10.entity.TenKaggleDiabetes;
import com.cqupt.software_10.mapper.TenKaggleDiabetesMapper;
import com.cqupt.software_10.service.TenKaggleDiabetesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenKaggleDiabetesServiceImpl implements TenKaggleDiabetesService {

    @Autowired
    private TenKaggleDiabetesMapper tenKaggleDiabetesMapper;

    @Override
    public List<TenKaggleDiabetes> getAllKaggleDiabetesService() {
        List<TenKaggleDiabetes> tenAllKaggleDiabetes = tenKaggleDiabetesMapper.getAllKaggleDiabetes();

        return tenAllKaggleDiabetes;
    }
}
