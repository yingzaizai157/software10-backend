package com.cqupt.software_10.controller.knowledge;


import com.cqupt.software_10.common.R;
import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.entity.data.ManageData;
import com.cqupt.software_10.entity.knowledge.Features;
import com.cqupt.software_10.service.knowledge.FeaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ten/knowledge")
public class FeaturesController {
    @Autowired
    private FeaturesService featuresService;

    @GetMapping("/featuresKnowledge")
    public R<List<String>> getExceptionKnowledge(@RequestParam("disease") String disease){
        List<String> fields = featuresService.getAllFeaturesENName();
        List<String> exceptions = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            String exception = featuresService.getExceptionKnowledge(disease, fields.get(i));
            if (exception != null){
                System.out.println(exception);
                exceptions.add(exception);

            }
        }

        return new R<>(200,"成功",exceptions, exceptions.size());
    }

    //用以首页统计使用
    @GetMapping("/knowledgeNum")
    public Result knowledgeNum(){
        List<Features> tableList = featuresService.getAllFeaturesKnowledges();
        return Result.success(tableList.size());
    }


    @GetMapping("/e_features")
    public R<List<Features>> getAllFeatures(){
        List<Features> tableList = featuresService.getAllFeaturesKnowledges();
        return new R<>(200,"成功",tableList, tableList.size());
    }

    @GetMapping("/disease")
    public R<List<String>> getKnowledgeDisease(){
        List<String> diseases = featuresService.getKnowledgeDisease();
        return new R<>(200,"成功",diseases, diseases.size());
    }

    @GetMapping("/risks")
    public R<List<String>> getKnowledgeFactorCN(){
        List<String> risks = featuresService.getKnowledgeFactorCN();
        return new R<>(200,"成功",risks, risks.size());
    }

    @GetMapping("/delete/{id}")
    public void deleteDataSets(@PathVariable("id") int id){
        featuresService.deleteKnowledgeById(id);
    }

    @GetMapping("/search")
    public R<List<Features>> getSearchDataSets(
            @RequestParam("disease_name") String diseaseName,
            @RequestParam("risks") String risks
    ){
        List<Features> searchedKnowledge = featuresService.searchByDiseaseName(diseaseName, risks);
        return new R<>(200,"成功",searchedKnowledge, searchedKnowledge.size());
    }

    @GetMapping("/update")
    public R<List<Features>> updateSearchById(
            @RequestParam("id") int id,
            @RequestParam("diseaseName") String diseaseName,
            @RequestParam("riskFactorsCn") String riskFactorsCn,
            @RequestParam("isException") int isException,
            @RequestParam("exceptionLow") double exceptionLow,
            @RequestParam("exceptionUp") double exceptionUp
    ){
        // System.out.println(id + " " +  tableName + " " +  disease + " " +  creator + " " +  remark);
        Features features = new Features(
                id, diseaseName, riskFactorsCn,
                isException, exceptionLow, exceptionUp);

        featuresService.updateKnowledgeInfo(features);
        List<Features> knowledges = featuresService.getAllFeaturesKnowledges();

        return new R<>(200,"成功",knowledges, knowledges.size());
    }

    @GetMapping("/add")
    public R<List<Features>> addKnowledge(

            @RequestParam("diseaseName") String diseaseName,
            @RequestParam("riskFactorsCn") String riskFactorsCn,
            @RequestParam("isException") int isException,
            @RequestParam("exceptionLow") double exceptionLow,
            @RequestParam("exceptionUp") double exceptionUp,
            @RequestParam("exceptionExplain") String exceptionExplain,
            @RequestParam("resource") String resource,
            @RequestParam("address") String address
    ){
        // System.out.println(id + " " +  tableName + " " +  disease + " " +  creator + " " +  remark);
        Features features = new Features(
                diseaseName, riskFactorsCn,
                isException, exceptionLow, exceptionUp);

        featuresService.addKnowledgeInfo(features);
        List<Features> knowledges = featuresService.getAllFeaturesKnowledges();

        return new R<>(200,"成功",knowledges, knowledges.size());
    }


//    @GetMapping("/rates")
//    public R<List<Map<String, String>>> getRates(){
//        List<Map<String, String>> res = new ArrayList<>();
//        List<Features> tableList = featuresService.getAllFeaturesKnowledges();
//        for (int i = 0; i < tableList.size(); i++) {
//            double num = tableList.get(i).getModelRate() * 100;
//            String  str = String.format("%.2f",num);
//            Map<String, String> map = new HashMap<>();
//            map.put("riskFactor", tableList.get(i).getRiskFactorsCn());
//            map.put("rate", str);
//            res.add(map);
//        }
//        return new R<>(200,"成功",res, res.size());
//    }

//    @GetMapping("/factor_rates")
//    public R<List<Map<String, String>>> getFactorRates(){
//        List<Map<String, String>> res = new ArrayList<>();
//        List<Features> tableList = featuresService.getAllFeaturesKnowledges();
//        for (int i = 0; i < tableList.size(); i++) {
//            double num = tableList.get(i).getModelRate();
//            String  str = String.format("%.2f",num);
//            Map<String, String> map = new HashMap<>();
//            map.put("riskFactor", tableList.get(i).getRiskFactorsCn());
//            map.put("rate", str);
//            res.add(map);
//        }
//        return new R<>(200,"成功",res, res.size());
//    }


//    @PostMapping("/updateDoctorRate")
//    public void updateDoctorRate(@RequestBody Map<String, String> params){
//        System.out.println(params);
//        Features feature = new Features();
//        feature.setId(Integer.parseInt(params.get("id")));
//        feature.setDoctorRate(Double.parseDouble(params.get("doctorRate")));
//        System.out.println(feature);
//        featuresService.setDoctorRate(feature);
//        System.out.println("======医生反馈成功=======");
//    }


}
