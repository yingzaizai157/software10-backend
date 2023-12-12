package com.cqupt.software_10.controller;


import com.alibaba.fastjson.JSONObject;
import com.cqupt.software_10.entity.response.RuntimeBusServiceResponse;
import com.cqupt.software_10.service.RuntimeBusService;
import com.cqupt.software_10.service.RuntimeTaskService;
import com.cqupt.software_10.service.TenKaggleDiabetesReflectService;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/runtime_bus")
//@RequestMapping("/run")
class RuntimeBusController {
    @Autowired
    private TenKaggleDiabetesReflectService kaggleDiabetesReflectService;

    @Resource
    private RuntimeBusService runtimeBusService;

    @Resource
    private RuntimeTaskService runtimeTaskService;

    @PostMapping("/xgboost")
    public Map<String, Double> submitPredictModel(
            @RequestBody String tableName
    ) throws Exception {
        JSONObject object = JSONObject.parseObject(tableName);
        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitBus(object.getString("tableName"));

        System.out.println("======submit_model========");
        Map<String, Double> mapRes = new HashMap<>();

        String acc = runtimeBusServiceResponse.getRes().get(0);


        System.out.println(runtimeBusServiceResponse.getRes());

        acc = acc.substring(1, acc.length() - 1);
        System.out.println(acc);
        mapRes.put("acc", Double.parseDouble(acc.split(":")[1]));

        String ret = runtimeBusServiceResponse.getRes().get(1);
        ret = ret.substring(1, ret.length() - 1);
        System.out.println(ret);
        String[] parts = ret.split(",");
        for(String part: parts){
            String[] sp = part.split(":");
            System.out.println("sp[0]: " + sp[0]);
            String cnName = kaggleDiabetesReflectService.getKaggleDiabetesReflectCnByEn(sp[0]);
            mapRes.put(cnName, Double.parseDouble(sp[1]));
        }

        System.out.println(mapRes);

        return mapRes;
    }

    @PostMapping("/rl_model")
    public Map<String, Double> submitRLModel() throws Exception {

        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitRLModel();

        System.out.println("======RLModel========");
        Map<String, Double> mapRes = new HashMap<>();

        String acc = runtimeBusServiceResponse.getRes().get(0);
        acc = acc.substring(1, acc.length() - 1);
        System.out.println(acc);
        mapRes.put("acc", Double.parseDouble(acc.split(":")[1]));

        String ret = runtimeBusServiceResponse.getRes().get(1);
        ret = ret.substring(1, ret.length() - 1);
        System.out.println(ret);
        String[] parts = ret.split(",");
        for(String part: parts){
            String[] sp = part.split(":");
            System.out.println("sp[0]: " + sp[0]);
            String cnName = kaggleDiabetesReflectService.getKaggleDiabetesReflectCnByEn(sp[0]);
            mapRes.put(cnName, Double.parseDouble(sp[1]));
        }

        System.out.println(mapRes);

        return mapRes;
    }

    @PostMapping("/rl_model2")
    public Map<String, Double> submitRLModel2() throws Exception {

        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitRLModel2();

        System.out.println("======RLModel2========");
        Map<String, Double> mapRes = new HashMap<>();

        String acc = runtimeBusServiceResponse.getRes().get(0);
        acc = acc.substring(1, acc.length() - 1);
        System.out.println(acc);
        mapRes.put("acc", Double.parseDouble(acc.split(":")[1]));

        String ret = runtimeBusServiceResponse.getRes().get(1);
        ret = ret.substring(1, ret.length() - 1);
        System.out.println(ret);
        String[] parts = ret.split(",");
        for(String part: parts){
            String[] sp = part.split(":");
            System.out.println("sp[0]: " + sp[0]);
            String cnName = kaggleDiabetesReflectService.getKaggleDiabetesReflectCnByEn(sp[0]);
            mapRes.put(cnName, Double.parseDouble(sp[1]));
        }

        System.out.println(mapRes);

        return mapRes;
    }

    @PostMapping("/dqn/{rate}")
    public Map<String, Double> submitDQN(
            @PathVariable("rate") String rate
    ) throws Exception {

        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitDQN(rate);

        System.out.println(runtimeBusServiceResponse);

        System.out.println("======DQN========");
        Map<String, Double> mapRes = new HashMap<>();

        String acc = runtimeBusServiceResponse.getRes().get(0);
        acc = acc.substring(1, acc.length() - 1);
        System.out.println(acc);
        mapRes.put("rate", Double.parseDouble(acc.split(":")[1]));

        System.out.println(mapRes);

        return mapRes;
    }

    @PostMapping("/dqn_pred/{model}")
    public Map<String, Double> submitDQNPred(
            @PathVariable("model") String model
    ) throws Exception {

        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitDQNPred(model);

        System.out.println(runtimeBusServiceResponse);

        System.out.println("======DQNPred========");
        Map<String, Double> mapRes = new HashMap<>();

        String acc = runtimeBusServiceResponse.getRes().get(0);
        acc = acc.substring(1, acc.length() - 1);
        System.out.println(acc);
        mapRes.put("model", Double.parseDouble(acc.split(":")[1]) * 100 );

        System.out.println(mapRes);

        return mapRes;
    }

}
