package com.cqupt.software_10.controller;


import com.alibaba.fastjson.JSONObject;
import com.cqupt.software_10.entity.AlgorithmParameter;
import com.cqupt.software_10.entity.response.RuntimeBusServiceResponse;
import com.cqupt.software_10.entity.user.User;
import com.cqupt.software_10.service.LogService;
import com.cqupt.software_10.service.RuntimeBusService;
import com.cqupt.software_10.service.RuntimeTaskService;
import com.cqupt.software_10.service.TenKaggleDiabetesReflectService;
import com.cqupt.software_10.service.tasks.MyTaskService;
import com.cqupt.software_10.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

import com.google.gson.Gson;

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

    @Autowired
    private MyTaskService myTaskService;

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

//    @PostMapping("/xgboost")
//    public Map<String, Double> submitPredictModel(
//            @RequestBody String tableName
//    ) throws Exception {
//        JSONObject object = JSONObject.parseObject(tableName);
//        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitBus(object.getString("tableName"));
//
//        System.out.println("======submit_model========");
//        Map<String, Double> mapRes = new HashMap<>();
//
//        String acc = runtimeBusServiceResponse.getRes().get(0);
//
//
//        System.out.println(runtimeBusServiceResponse.getRes());
//
//        acc = acc.substring(1, acc.length() - 1);
//        System.out.println(acc);
//        mapRes.put("acc", Double.parseDouble(acc.split(":")[1]));
//
//        String ret = runtimeBusServiceResponse.getRes().get(1);
//        ret = ret.substring(1, ret.length() - 1);
//        System.out.println(ret);
//        String[] parts = ret.split(",");
//        for(String part: parts){
//            String[] sp = part.split(":");
//            System.out.println("sp[0]: " + sp[0]);
//            String cnName = kaggleDiabetesReflectService.getKaggleDiabetesReflectCnByEn(sp[0]);
//            mapRes.put(cnName, Double.parseDouble(sp[1]));
//        }
//
//        System.out.println(mapRes);
//
//        return mapRes;
//    }


    // 此方法用以获取特征初始权重
    @PostMapping("/submitBus")
    public List<Map<String, String>> submitPredictModel(
            @RequestBody String parameters,
            @RequestParam String curUid
    ) throws Exception {
        JSONObject object = JSONObject.parseObject(parameters);

        String tableName = object.getString("tableName");
        List<String> cols = object.getJSONArray("features").toJavaList(String.class);
        List<String> labels = object.getJSONArray("labels").toJavaList(String.class);
        String alg = object.getString("alg");
        String mode = object.getString("mode");
//        System.out.println("tableName:"+tableName);
//        System.out.println("cols:"+cols);
//        System.out.println("labels:"+labels);


        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitBus(tableName, cols, labels, alg, mode);

        System.out.println("======submit_model========");

//        String acc = runtimeBusServiceResponse.getRes().get(0);


//        System.out.println(runtimeBusServiceResponse.getRes());
//
//        acc = acc.substring(1, acc.length() - 1);
//        System.out.println(acc);
//        mapRes.put("acc", Double.parseDouble(acc.split(":")[1]));
        List<String> temp = runtimeBusServiceResponse.getRes();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();

        for (int i = 0; i < temp.size(); i++) {
            String ret = temp.get(i);
            Map<String, String> mapRes = new HashMap<>();
            ret = ret.substring(1, ret.length() - 1);
            String[] parts = ret.split(",");
            for(String part: parts){
                String[] sp = part.split(":");
//            String cnName = kaggleDiabetesReflectService.getKaggleDiabetesReflectCnByEn(sp[0]);
                mapRes.put("rate", sp[1]);
                mapRes.put("riskFactor", sp[0].substring(1, sp[0].length() - 1));
            }
            listMap.add(mapRes);
        }

//        String ret = runtimeBusServiceResponse.getRes().get(0);
//        ret = ret.substring(1, ret.length() - 1);
//        System.out.println(ret);
//        String[] parts = ret.split(",");
//        for(String part: parts){
//            String[] sp = part.split(":");
//            System.out.println("sp[0]: " + sp[0]);
////            String cnName = kaggleDiabetesReflectService.getKaggleDiabetesReflectCnByEn(sp[0]);
//            mapRes.put(sp[0], Double.parseDouble(sp[1]));
//        }

        System.out.println(listMap);

        User curUser = userService.getUserById(curUid);
        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，使用算法" + alg + "，应用的数据表名：" + tableName);
        return listMap;
    }



    @PostMapping("/getLackinfos_features")
    public List<Map<String, String>> getLackinfos_features(
            @RequestBody(required=false)  String arg,
            @RequestParam String curUid
    ) throws Exception {
        Gson gson = new Gson();
        Map map = gson.fromJson(arg, Map.class);

        String tableName = map.get("tableName").toString();
        String modename = map.get("modename").toString(); // 注：这里的modename是模式名称
        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitGetLackInfos(tableName, modename);

        System.out.println("======submit_model========");


//        String acc = runtimeBusServiceResponse.getRes().get(0);


//        System.out.println(runtimeBusServiceResponse.getRes());
//
//        acc = acc.substring(1, acc.length() - 1);
//        System.out.println(acc);
//        mapRes.put("acc", Double.parseDouble(acc.split(":")[1]));

        String temp = runtimeBusServiceResponse.getRes().get(0);
        String[] a = temp.substring(1, temp.length() - 1).split(",");
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();

        for (int i = 0; i < a.length; i++) {
            String ret = a[i];
            Map<String, String> mapRes = new HashMap<>();
            ret = ret.substring(1, ret.length() - 1);
            String[] parts = ret.split(",");
            for(String part: parts){
                String[] sp = part.split(":");
                mapRes.put("rate", sp[1]);
                mapRes.put("riskFactor", sp[0].substring(1, sp[0].length() - 1));
            }
            listMap.add(mapRes);
        }



//        String ret = runtimeBusServiceResponse.getRes().get(0);
//        ret = ret.substring(1, ret.length() - 1);
//        System.out.println(ret);
//        String[] parts = ret.split(",");
//        for(String part: parts){
//            String[] sp = part.split(":");
//            System.out.println("sp[0]: " + sp[0]);
////            String cnName = kaggleDiabetesReflectService.getKaggleDiabetesReflectCnByEn(sp[0]);
//            mapRes.put(sp[0], Double.parseDouble(sp[1]));
//        }

        System.out.println(listMap);

        User curUser = userService.getUserById(curUid);
        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，获得数据表各特征列的缺失信息。数据表的表名：" + tableName);
        return listMap;
    }



    @PostMapping("/getLackinfos_labels")
    public List<Map<String, String>> getLackinfos_labels(
            @RequestBody(required=false)  String arg,
            @RequestParam String curUid
    ) throws Exception {

        Gson gson = new Gson();
        Map map = gson.fromJson(arg, Map.class);

        String tableName = map.get("tableName").toString();
        String modename = map.get("modename").toString(); // 注：这里的modename是模式名称
        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitGetLackInfos(tableName, modename);
        System.out.println("======submit_model========");


//        String acc = runtimeBusServiceResponse.getRes().get(0);


//        System.out.println(runtimeBusServiceResponse.getRes());
//
//        acc = acc.substring(1, acc.length() - 1);
//        System.out.println(acc);
//        mapRes.put("acc", Double.parseDouble(acc.split(":")[1]));

        String temp = runtimeBusServiceResponse.getRes().get(1);
        String[] a = temp.substring(1, temp.length() - 1).split(",");
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();

        for (int i = 0; i < a.length; i++) {
            String ret = a[i];
            Map<String, String> mapRes = new HashMap<>();
            ret = ret.substring(1, ret.length() - 1);
            String[] parts = ret.split(",");
            for(String part: parts){
                String[] sp = part.split(":");
                mapRes.put("rate", sp[1]);
                mapRes.put("riskFactor", sp[0].substring(1, sp[0].length() - 1));
            }
            listMap.add(mapRes);
        }



//        String ret = runtimeBusServiceResponse.getRes().get(0);
//        ret = ret.substring(1, ret.length() - 1);
//        System.out.println(ret);
//        String[] parts = ret.split(",");
//        for(String part: parts){
//            String[] sp = part.split(":");
//            System.out.println("sp[0]: " + sp[0]);
////            String cnName = kaggleDiabetesReflectService.getKaggleDiabetesReflectCnByEn(sp[0]);
//            mapRes.put(sp[0], Double.parseDouble(sp[1]));
//        }

        System.out.println(listMap);

        User curUser = userService.getUserById(curUid);
        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，获得数据表各标签列的缺失信息。数据表的表名：" + tableName);
        return listMap;
    }




    @PostMapping("/RFAlg")
    public List<Map<String, String>> RFAlg(
            @RequestBody(required=false)  String parameters
    ) throws Exception {
        JSONObject object = JSONObject.parseObject(parameters);

        String tableName = object.getString("tableName");
        List<String> cols = object.getJSONArray("features").toJavaList(String.class);
        List<String> labels = object.getJSONArray("labels").toJavaList(String.class);
        System.out.println("tableName:"+tableName);
        System.out.println("cols:"+cols);
        System.out.println("labels:"+labels);

        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitRFAlg(tableName);
        System.out.println("======submit_model========");
//        String acc = runtimeBusServiceResponse.getRes().get(0);


//        System.out.println(runtimeBusServiceResponse.getRes());
//
//        acc = acc.substring(1, acc.length() - 1);
//        System.out.println(acc);
//        mapRes.put("acc", Double.parseDouble(acc.split(":")[1]));

        List<String> temp = runtimeBusServiceResponse.getRes();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();

        for (int i = 0; i < temp.size(); i++) {
            String ret = temp.get(i);
            Map<String, String> mapRes = new HashMap<>();
            ret = ret.substring(1, ret.length() - 1);
            String[] parts = ret.split(",");
            for(String part: parts){
                String[] sp = part.split(":");
//            String cnName = kaggleDiabetesReflectService.getKaggleDiabetesReflectCnByEn(sp[0]);
                mapRes.put("rate", sp[1]);
                mapRes.put("riskFactor", sp[0].substring(1, sp[0].length() - 1));
            }
            listMap.add(mapRes);
        }
//        String ret = runtimeBusServiceResponse.getRes().get(0);
//        ret = ret.substring(1, ret.length() - 1);
//        System.out.println(ret);
//        String[] parts = ret.split(",");
//        for(String part: parts){
//            String[] sp = part.split(":");
//            System.out.println("sp[0]: " + sp[0]);
////            String cnName = kaggleDiabetesReflectService.getKaggleDiabetesReflectCnByEn(sp[0]);
//            mapRes.put(sp[0], Double.parseDouble(sp[1]));
//        }
        System.out.println(listMap);

        return listMap;
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

//    @PostMapping("/dqn/{rate}")
//    public Map<String, Double> submitDQN(
//            @PathVariable("rate") String rate
//    ) throws Exception {
//
//        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitDQN(rate);
//
//        System.out.println(runtimeBusServiceResponse);
//
//        System.out.println("======DQN========");
//        Map<String, Double> mapRes = new HashMap<>();
//
//        String acc = runtimeBusServiceResponse.getRes().get(0);
//        acc = acc.substring(1, acc.length() - 1);
//        System.out.println(acc);
//        mapRes.put("rate", Double.parseDouble(acc.split(":")[1]));
//
//        System.out.println(mapRes);
//
//        return mapRes;
//    }

//    @PostMapping("/svm")
//    public Map<String, String> submitSVM(
//            @RequestBody(required=false)  String arg
//    ) throws Exception {
//        arg = arg.substring(1, arg.length() - 3);
//        String[] a = arg.split(",");
//
//        String kernel = a[0].split(":")[1];
//        String random_state = a[1].split(":")[1];
////        String paramRange = a[2].split(":")[1];
//        String cv = a[3].split(":")[1];
//
////        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitSVM(kernel, random_state, paramRange, cv);
//        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitSVM(kernel, random_state, cv);
//
//        System.out.println("======submit_model========");
//
//        List<String> temp = runtimeBusServiceResponse.getRes();
//        System.out.println(temp);
//
//        Map<String, String> mapRes = new HashMap<>();
//        for (int i = 0; i < temp.size(); i++) {
//            String ret = temp.get(i);
//            ret = ret.substring(1, ret.length() - 1);
//            String[] parts = ret.split(",");
//            for(String part: parts){
//                String[] sp = part.split(":");
//                mapRes.put(sp[0].substring(1, sp[0].length() - 1), sp[1].substring(1, sp[1].length()));
//            }
//        }
//
//        System.out.println(mapRes);
//
//        return mapRes;
//    }

//    @PostMapping("/knn")
//    public Map<String, String> submitKNN(
//            @RequestBody(required=false)  String arg
//    ) throws Exception {
//        arg = arg.substring(1, arg.length() - 3);
//        String[] a = arg.split(",");
//
//        String paramRange = a[0].split(":")[1];
//        String cv = a[1].split(":")[1];
//
//        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitKNN(paramRange, cv);
//
//        System.out.println("======submit_model========");
//
//        List<String> temp = runtimeBusServiceResponse.getRes();
//        System.out.println(temp);
//
//        Map<String, String> mapRes = new HashMap<>();
//        for (int i = 0; i < temp.size(); i++) {
//            String ret = temp.get(i);
//            ret = ret.substring(1, ret.length() - 1);
//            String[] parts = ret.split(",");
//            for(String part: parts){
//                String[] sp = part.split(":");
//                mapRes.put(sp[0].substring(1, sp[0].length() - 1), sp[1].substring(1, sp[1].length()));
//            }
//        }
//
//        System.out.println(mapRes);
//
//        return mapRes;
//    }





    @PostMapping("/submitAlg")
    public Map<String,Map<String, String>> submitAlg(
            @RequestBody(required=false)  List<AlgorithmParameter> parameters, @RequestParam String curUid
    ) throws Exception {
        Map<String,Map<String, String>> result = new HashMap<String,Map<String, String>>();
        for (AlgorithmParameter parameter : parameters) {
            Map<String, String> mapRes = new HashMap<>();
            String name = parameter.getName();
            Map<String, Object> params = parameter.getForms();

            System.out.println(parameter);
            if (name.equals("dqn")) {
                // dqn参数
                String reward = params.get("reward") + "";
                String epoch = params.get("epoch") + "";
                String gamma = params.get("gamma") + "";
                String learning_rate = params.get("learning_rate") + "";
                String modelName = params.get("taskname") + "";
                String table_name = params.get("table_name") + "";
                List<String> cols = (List<String>) params.get("cols");
                List<String> labels = (List<String>) params.get("labels");
                // dqn模型运行
                System.out.println("======dqn========");
                System.out.println("reward, epoch, gamma, learning_rate, modelName, table_name, cols, labels"+ reward+ epoch+ gamma+ learning_rate+ modelName+ table_name+ cols+ labels);
                RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitDQN(reward, epoch, gamma, learning_rate, modelName, table_name, cols, labels);
                List<String> temp = runtimeBusServiceResponse.getRes();
                for (String ret : temp) {
                    ret = ret.substring(1, ret.length() - 1);
                    String[] parts = ret.split(",");
                    for (String part : parts) {
                        String[] sp = part.split(":");
                        String strkey = sp[0].substring(1, sp[0].length() - 1);
                        if (strkey.equals("total_losses") || strkey.equals("total_rewards")) {
                            String strvalue = sp[1].substring(2, sp[1].length() - 1);
                            strvalue = strvalue.replaceAll("p", ",");
                            String[] value = strvalue.split(",");
                            Integer total = value.length;
                            strvalue = "";
                            for (int j = 0; j < total; j++) {
                                strvalue += "[['" + String.valueOf(j + 1) + "'," + value[j] + "]],";
                            }
                            mapRes.put(strkey, strvalue.substring(0, strvalue.length() - 1));
                        } else if (strkey.equals("cols")) {
                        } else if (strkey.equals("avg_shapvalue")) {
                            String strvalue = sp[1].substring(2, sp[1].length() - 1);
                            strvalue = strvalue.replaceAll("p", ",");
                            String[] value = strvalue.split(",");
                            Integer total = value.length;
                            strvalue = "";
                            for (int j = 0; j < total; j++) {
                                strvalue += "{value:" + value[j] + ",name:" + cols.get(j) + "},";
                            }
                            mapRes.put(strkey, strvalue.substring(0, strvalue.length() - 1));
                        } else {
                            mapRes.put(strkey, sp[1].substring(1, sp[1].length()));
                        }
                    }
                }

            }
            else if (name.equals("svm")) {
                // SVM参数
                String kernel = (String) params.get("kernel");
                String random_state = params.get("random_state") + "";
                String cv = params.get("cv") + "";
                String modelName = (String) params.get("taskname");
                String table_name = (String) params.get("table_name");
                List<String> cols = (List<String>) params.get("cols");
                List<String> labels = (List<String>) params.get("labels");
                //svm模型运行
                System.out.println("======svm========");
                RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitSVM(kernel, random_state, cv, modelName, table_name, cols, labels);
                List<String> temp = runtimeBusServiceResponse.getRes();
                for (String ret : temp) {
                    ret = ret.substring(1, ret.length() - 1);
                    String[] parts = ret.split(",");
                    for (String part : parts) {
                        String[] sp = part.split(":");
                        String strkey = sp[0].substring(1, sp[0].length() - 1);
                        if (strkey.equals("total_losses") || strkey.equals("total_rewards")) {
                            String strvalue = sp[1].substring(2, sp[1].length() - 1);
                            strvalue = strvalue.replaceAll("p", ",");
                            String[] value = strvalue.split(",");
                            Integer total = value.length;
                            strvalue = "";
                            for (int j = 0; j < total; j++) {
                                strvalue += "{value:" + value[j] + ",name:" + cols.get(j) + "},";
                            }
                            mapRes.put(strkey, strvalue.substring(0, strvalue.length() - 1));
                        } else if (strkey.equals("cols")) {
                        } else if (strkey.equals("avg_shapvalue")) {
                            String strvalue = sp[1].substring(2, sp[1].length() - 1);
                            strvalue = strvalue.replaceAll("p", ",");
                            String[] value = strvalue.split(",");
                            Integer total = value.length;
                            strvalue = "";
                            for (int j = 0; j < total; j++) {
                                strvalue += "{value:" + value[j] + ",name:" + cols.get(j) + "},";
                            }
                            mapRes.put(strkey, strvalue.substring(0, strvalue.length() - 1));
                        }
                        else {
                            mapRes.put(strkey, sp[1].substring(1, sp[1].length()));
                        }
                    }
                }

            }
            else if (name.equals("knn")) {
                //KNN参数
//                String paramRange = String.valueOf(params.get("paramRange"));
//                String cv = String.valueOf(params.get("cv"));
                String K = params.get("K") + "";
                String random_state = params.get("random_state") + "";
                String cv = params.get("cv") + "";
                String modelName = (String) params.get("taskname");
                String table_name = (String) params.get("table_name");
                List<String> cols = (List<String>) params.get("cols");
                List<String> labels = (List<String>) params.get("labels");
                // knn模型运行
                System.out.println("======knn========");
                RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitKNN(K, random_state, cv, modelName, table_name, cols, labels);
                List<String> temp = runtimeBusServiceResponse.getRes();
                for (String ret : temp) {
                    ret = ret.substring(1, ret.length() - 1);
                    String[] parts = ret.split(",");
                    for (String part : parts) {
                        String[] sp = part.split(":");
                        String strkey = sp[0].substring(1, sp[0].length() - 1);
                        if (strkey.equals("total_losses") || strkey.equals("total_rewards")) {
                            String strvalue = sp[1].substring(2, sp[1].length() - 1);
                            strvalue = strvalue.replaceAll("p", ",");
                            String[] value = strvalue.split(",");
                            Integer total = value.length;
                            strvalue = "";
                            for (int j = 0; j < total; j++) {
                                strvalue += "{value:" + value[j] + ",name:" + cols.get(j) + "},";
                            }
                            mapRes.put(strkey, strvalue.substring(0, strvalue.length() - 1));
                        } else if (strkey.equals("cols")) {
                        } else if (strkey.equals("avg_shapvalue")) {
                            String strvalue = sp[1].substring(2, sp[1].length() - 1);
                            strvalue = strvalue.replaceAll("p", ",");
                            String[] value = strvalue.split(",");
                            Integer total = value.length;
                            strvalue = "";
                            for (int j = 0; j < total; j++) {
                                strvalue += "{value:" + value[j] + ",name:" + cols.get(j) + "},";
                            }
                            mapRes.put(strkey, strvalue.substring(0, strvalue.length() - 1));
                        } else {
                            mapRes.put(strkey, sp[1].substring(1, sp[1].length()));
                        }
                    }
                }
            }
            result.put(name, mapRes);

            User curUser = userService.getUserById(curUid);
            logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，运行算法" + name + "，应用的数据表名：" + params.get("table_name") + "，任务名：" + params.get("taskname"));

        }
        return result;
    }


    @PostMapping("/dqn")
    public Map<String, String> submitDQN(
            @RequestBody(required=false)  String parameters
    ) throws Exception {
        JSONObject object = JSONObject.parseObject(parameters);

        String reward = object.getString("reward");
        String epoch = object.getString("epoch");
        String gamma = object.getString("gamma");
        String learning_rate = object.getString("learning_rate");
        String modelName = object.getString("modelName");
        String table_name = object.getString("table_name");
        List<String> cols = object.getJSONArray("cols").toJavaList(String.class);
        List<String> labels = object.getJSONArray("labels").toJavaList(String.class);

        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.submitDQN(reward, epoch, gamma, learning_rate, modelName, table_name, cols, labels);

        System.out.println("======submit_model========");

        List<String> temp = runtimeBusServiceResponse.getRes();
        System.out.println(temp);

        Map<String, String> mapRes = new HashMap<>();
        for (int i = 0; i < temp.size(); i++) {
            String ret = temp.get(i);
            ret = ret.substring(1, ret.length() - 1);
            String[] parts = ret.split(",");
            for(String part: parts){
                String[] sp = part.split(":");
                String strkey = sp[0].substring(1, sp[0].length() - 1);
                if (strkey.equals("total_losses") || strkey.equals("total_rewards")){
                    String strvalue = sp[1].substring(2, sp[1].length()-1);
                    strvalue = strvalue.replaceAll("p",",");
                    String[] value = strvalue.split(",");
                    Integer total = value.length;
                    strvalue = "";
                    for (int j = 0; j < total; j++) {
                        strvalue += "['" + String.valueOf(j+1) + "'," + value[j] + "],";
                    }
                    mapRes.put(strkey, strvalue.substring(0,strvalue.length()-1));
                }
                else {
                    mapRes.put(strkey, sp[1].substring(1, sp[1].length()));
                }
            }
        }



        System.out.println(mapRes);

        return mapRes;
    }


    @PostMapping("/runmodel")
    public Map<String, String> runDQN(
            @RequestBody(required=false)  String parameters, @RequestParam String curUid
    ) throws Exception {
        JSONObject object = JSONObject.parseObject(parameters);

        String modelname = object.getString("modelname");
        String taskname = object.getString("taskname");
        List<String> onedata = object.getJSONArray("featuredata").toJavaList(String.class);

//        String feature = myTaskService.getFeatureByTasknameAndModelname(taskname, modelname);
//        String[] feature_arr = feature.substring(2, feature.length() - 2).split("\",\"");
//        List<String> feature_list = Arrays.asList(feature_arr);
//        List<String> onedata = new ArrayList<String>();
//        for (int i = 0; i < feature_list.size(); i++) {
//            onedata.add(object.getString(feature_list.get(i)));
//        }



        RuntimeBusServiceResponse runtimeBusServiceResponse = null;
        //根据modelname运行不同的模型
        if (modelname.equals("dqn")) {
            runtimeBusServiceResponse = runtimeBusService.rundqn(modelname, taskname, onedata);
            System.out.println("======onepre_dqn========");
        }else if (modelname.equals("knn")) {
            runtimeBusServiceResponse = runtimeBusService.runknn(modelname, taskname, onedata);
            System.out.println("======onepre_knn========");
        }else if (modelname.equals("svm")) {
            runtimeBusServiceResponse = runtimeBusService.runsvm(modelname, taskname, onedata);
            System.out.println("======onepre_svm========");
        }

        List<String> temp = runtimeBusServiceResponse.getRes();
        Map<String, String> mapRes = new HashMap<>();
        for (int i = 0; i < temp.size(); i++) {
            String ret = temp.get(i);
            ret = ret.substring(1, ret.length() - 1);
            String[] parts = ret.split(",");
            for(String part: parts){
                String[] sp = part.split(":");
                mapRes.put(sp[0].substring(1, sp[0].length() - 1), sp[1].substring(1, sp[1].length()));
            }
        }


        User curUser = userService.getUserById(curUid);
        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，运行任务，任务名称：" + taskname + "，算法名称：" + modelname);
        return mapRes;
    }




//    @PostMapping("/rundqn")
//    public Map<String, String> runDQN(
//            @RequestBody(required=false)  String parameters
//    ) throws Exception {
//        JSONObject object = JSONObject.parseObject(parameters);
//
//        String model = object.getString("model");
//        List<String> onedata = object.getJSONArray("onedata").toJavaList(String.class);
//
//        RuntimeBusServiceResponse runtimeBusServiceResponse = runtimeBusService.rundqn(model, onedata);
//
//        System.out.println("======submit_model========");
//
//        List<String> temp = runtimeBusServiceResponse.getRes();
//        System.out.println(temp);
//
//        Map<String, String> mapRes = new HashMap<>();
//        for (int i = 0; i < temp.size(); i++) {
//            String ret = temp.get(i);
//            ret = ret.substring(1, ret.length() - 1);
//            String[] parts = ret.split(",");
//            for(String part: parts){
//                String[] sp = part.split(":");
//                mapRes.put(sp[0].substring(1, sp[0].length() - 1), sp[1].substring(1, sp[1].length()));
//            }
//        }
//
//        System.out.println(mapRes);
//
//        return mapRes;
//    }





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
