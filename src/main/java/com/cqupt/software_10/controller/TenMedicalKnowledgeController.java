package com.cqupt.software_10.controller;

import com.cqupt.software_10.common.R;
import com.cqupt.software_10.entity.TenKnowledgeFeature;
import com.cqupt.software_10.entity.TenKnowledgeFeatureExplain;
import com.cqupt.software_10.entity.TenMedicalKnowledgeBase;
import com.cqupt.software_10.service.TenKnowledgeFeatureExplainService;
import com.cqupt.software_10.service.TenKnowledgeFeatureService;
import com.cqupt.software_10.service.TenMedicalKnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ten/knowledge")
public class TenMedicalKnowledgeController {
    @Autowired
    private TenMedicalKnowledgeBaseService tenMedicalKnowledgeBaseService;

    @Autowired
    private TenKnowledgeFeatureService tenKnowledgeFeatureService;

    @Autowired
    private TenKnowledgeFeatureExplainService tenKnowledgeFeatureExplainService;

    @GetMapping("/all_data")
    public R<List<TenMedicalKnowledgeBase>> getAllKnowledge(){
        List<TenMedicalKnowledgeBase> knowledge = tenMedicalKnowledgeBaseService.
                                                                    getAllMedicalKnowledge();

        return new R<>(200, "成功", knowledge, knowledge.size());
    }

    @GetMapping("/features")
    public R<List<TenKnowledgeFeature>> getAllKnowledgeFeatures(){
        List<TenKnowledgeFeature> knowledge = tenKnowledgeFeatureService.
                getAllKnowledgeFeature();

        System.out.println(knowledge);

        return new R<>(200, "成功", knowledge, knowledge.size());
    }

    @GetMapping("/features_names")
    public R<List<TenKnowledgeFeatureExplain>> getAllKnowledgeFeaturesNames(){
        List<TenKnowledgeFeatureExplain> knowledge = tenKnowledgeFeatureExplainService.
                getAllKnowledgeFeatureExplain();

        System.out.println(knowledge);

        return new R<>(200, "成功", knowledge, knowledge.size());
    }


}