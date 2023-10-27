package com.cqupt.software_10.service.impl.data;

import com.cqupt.software_10.entity.TenKaggleDiabetes;
import com.cqupt.software_10.mapper.data.KaggleDiabetesDataMapper;
import com.cqupt.software_10.service.data.KaggleDiabetesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KaggleDiabetesServiceImpl implements KaggleDiabetesService {
    @Autowired
    private KaggleDiabetesDataMapper kaggleDiabetesDataMapper;

    @Override
    public List<TenKaggleDiabetes> getAllKaggleDiabetesData() {
        List<TenKaggleDiabetes> kaggleDiabetes = kaggleDiabetesDataMapper.getAllKaggleDiabetesData();
        return kaggleDiabetes;
    }
}
