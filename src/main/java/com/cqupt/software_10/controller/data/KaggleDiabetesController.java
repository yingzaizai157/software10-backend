package com.cqupt.software_10.controller.data;


import com.cqupt.software_10.common.R;
import com.cqupt.software_10.entity.TenKaggleDiabetes;
import com.cqupt.software_10.service.data.KaggleDiabetesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ten/data")
public class KaggleDiabetesController {
    @Autowired
    private KaggleDiabetesService kaggleDiabetesService;

    @GetMapping("/diabetes")
    public R<List<TenKaggleDiabetes>> getAllKaggleDiabetesData(){
        List<TenKaggleDiabetes> KaggleDiabetesData = kaggleDiabetesService.getAllKaggleDiabetesData();
        return new R<>(200,"成功",KaggleDiabetesData, KaggleDiabetesData.size());
    }
}
