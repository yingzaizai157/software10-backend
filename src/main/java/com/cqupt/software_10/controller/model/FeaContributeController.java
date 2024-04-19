package com.cqupt.software_10.controller.model;

import com.cqupt.software_10.common.R;
import com.cqupt.software_10.entity.model.FeaContribute;
import com.cqupt.software_10.service.model.FeaContributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ten/feaContribute")
public class FeaContributeController {
    @Autowired
    private FeaContributeService feaContributeService;

    @GetMapping("/getBytableName")
    public R<List<Map<String, String>>> getAllTasks(
            @RequestParam("tableName") String tableName
    ){
        List<Map<String, String>> res = new ArrayList<>();
        List<FeaContribute> feaContribute = feaContributeService.getByTableName(tableName);
        for (FeaContribute contribute : feaContribute) {
            Map<String, String> map = new HashMap<>();
            map.put("riskFactor",contribute.getEnCol());
            map.put("rate", String.format("%.4f",contribute.getModelFeatruesContributions()*100));
            res.add(map);
        }
        return new R<>(200,"成功",res, res.size());
    }



















}
