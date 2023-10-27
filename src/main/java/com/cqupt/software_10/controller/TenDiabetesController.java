package com.cqupt.software_10.controller;

import com.cqupt.software_10.common.R;
import com.cqupt.software_10.service.TenDiabetesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/ten/diabetes")
public class TenDiabetesController {
    @Autowired
    private TenDiabetesService tenDiabetesService;

    @GetMapping("/riskfactor")
    public R<List<String>> getRiskFactorByTableName(
            @RequestParam("limitRiskFactor") Integer limitRiskFactor,
            HttpSession session
    ){
        List<String> tableList = tenDiabetesService.getRiskFactorByTableName(limitRiskFactor);
        return new R<>(200,"成功",tableList, tableList.size());
    }

    @GetMapping("/complications/{limitComplications}")
    public R<List<String>> getComplicationsByTableName(
            @PathVariable("limitComplications")Integer limitComplications
                ){
        List<String> tableList = tenDiabetesService.getComplicationsByTableName(limitComplications);
        return new R<>(200,"成功",tableList, tableList.size());
    }

    @GetMapping("/base/{base}")
    public R<List<String>> getBaseFactorByTableName(
            @PathVariable("base")Integer base
    ){
        List<String> tableList = tenDiabetesService.getBaseFactorByTableName(base);
        return new R<>(200,"成功",tableList, tableList.size());
    }

    @GetMapping("/check/{check}")
    public R<List<String>> getCheckFactorByTableName(
            @PathVariable("check")Integer check
    ){
        List<String> tableList = tenDiabetesService.getCheckFactorByTableName(check);
        return new R<>(200,"成功",tableList, tableList.size());
    }

}
