package com.cqupt.software_10.controller.model;

import com.cqupt.software_10.common.R;
import com.cqupt.software_10.entity.model.ModelInfo;
import com.cqupt.software_10.service.model.ModelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ten/model")
public class ModelInfoController {
    @Autowired
    private ModelInfoService modelInfoService;

    @GetMapping("/infos")
    public R<List<ModelInfo>> getModelInfos(){
        List<ModelInfo> tableList = modelInfoService.getAllInfo();
        return new R<>(200,"成功",tableList, tableList.size());
    }



    @GetMapping("/show/{table}/{reward}/{model}")
    public R<Integer> getModelShow(@PathVariable("table") String table,
                                           @PathVariable("reward") String reward,
                                           @PathVariable("model") String model){
        Integer performance = modelInfoService.getPerformanceByFactors(table, reward, model);


        return new R<>(200,"成功",performance,1);
    }

    @GetMapping("/index_show")
    public R<List<ModelInfo>> getModelIndexShow(){
        List<ModelInfo> tableList = modelInfoService.getAllInfo();
        for (ModelInfo info: tableList){
            System.out.println(info);
        }
        return new R<>(200,"成功",tableList, tableList.size());
    }

    @GetMapping("/tables")
    public R<List<Map<String, String>>> getTables(){
        List<String> tables = modelInfoService.getTables();
        List<Map<String, String>> res = new ArrayList<>();
        for (int i = 0; i < tables.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("value", tables.get(i));
            map.put("label", tables.get(i));
            map.put("key", tables.get(i));
            res.add(map);
        }
        System.out.println(res);
        return new R<>(200,"成功",res, res.size());
    }

    @GetMapping("/reward")
    public R<List<Map<String, String>>> getRewards(){
        List<String> tables = modelInfoService.getRewards();
        List<Map<String, String>> res = new ArrayList<>();
        for (int i = 0; i < tables.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("value", tables.get(i));
            map.put("label", tables.get(i));
            map.put("key", tables.get(i));
            res.add(map);
        }
        System.out.println(res);
        return new R<>(200,"成功",res, res.size());
    }

    @GetMapping("/name")
    public R<List<Map<String, String>>> getModelNames(){
        List<String> tables = modelInfoService.getModelName();
        List<Map<String, String>> res = new ArrayList<>();
        for (int i = 0; i < tables.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("value", tables.get(i));
            map.put("label", tables.get(i));
            map.put("key", tables.get(i));
            res.add(map);
        }
        System.out.println(res);
        return new R<>(200,"成功",res, res.size());
    }


}
