package com.cqupt.software_10.controller;

import com.cqupt.software_10.common.R;
import com.cqupt.software_10.entity.TenKaggleDiabetes;
import com.cqupt.software_10.entity.TenKaggleDiabetesReflect;
import com.cqupt.software_10.service.TenKaggleDiabetesReflectService;
import com.cqupt.software_10.service.TenKaggleDiabetesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ten/kaggle")
public class TenKaggleDiabetesController {

    @Autowired
    private TenKaggleDiabetesReflectService tenKaggleDiabetesReflectService;

    @Autowired
    private TenKaggleDiabetesService tenKaggleDiabetesService;

    @GetMapping("/all_reflect_column")
    public R<List<String>> getAllColumn(){
        List<String> kaggleColumn = tenKaggleDiabetesReflectService.getAllKaggleDiabetesReflectColumn();

        return new R<>(200, "成功", kaggleColumn, kaggleColumn.size());
    }

    @GetMapping("/all_reflect_data")
    public R<List<TenKaggleDiabetesReflect>> getAllReflectData(@RequestParam("model") String model){
        List<TenKaggleDiabetesReflect> reflectData = tenKaggleDiabetesReflectService.getAllKaggleDiabetesReflectData(model);

        System.out.println("================reflectdata===========");
        for(TenKaggleDiabetesReflect data: reflectData){
            System.out.println(data);
        }

        return new R<>(200, "成功", reflectData, reflectData.size());
    }

    @GetMapping("/all_diabetes_data/{page}")
    public R<List<TenKaggleDiabetes>> getAllKaggleDiabetesData(@PathVariable("page") int page){
        PageHelper.startPage(page, 10);
        List<TenKaggleDiabetes> diabetesData = tenKaggleDiabetesService.getAllKaggleDiabetesService();
        PageInfo pageInfo = new PageInfo(diabetesData);
        return new R<>(200,"成功",diabetesData,pageInfo.getPages());
    }

    @GetMapping("/update_riskFactor")
    public void updateRiskFactor(@RequestParam("model") String model,
                                 @RequestParam("colName") String colName,
                                 @RequestParam("value") double value){
        tenKaggleDiabetesReflectService.updateContributorByCnCol(model, colName, value);
    }



}
