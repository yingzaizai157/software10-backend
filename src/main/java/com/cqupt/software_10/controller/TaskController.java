package com.cqupt.software_10.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.common.TaskRequest;
import com.cqupt.software_10.entity.Task;
import com.cqupt.software_10.entity.tasks.AnalysisTask;
import com.cqupt.software_10.entity.tasks.Model;
import com.cqupt.software_10.entity.tasks.MyTask;
import com.cqupt.software_10.entity.user.User;
import com.cqupt.software_10.service.LogService;
import com.cqupt.software_10.service.TaskService;
import com.cqupt.software_10.service.tasks.MyTaskService;
import com.cqupt.software_10.service.user.UserService;
import com.cqupt.software_10.util.SecurityUtil;
import com.cqupt.software_10.vo.DateCount;
import com.cqupt.software_10.vo.TaskStaticVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/Task")
public class TaskController {
    @Autowired
    private  TaskService taskService;

    @Autowired
    private MyTaskService myTaskService;

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;


    @GetMapping("/all")
    public Result GetAll() {
        List<MyTask> allTasks = myTaskService.list();
        return Result.success(allTasks);
    }

    // 查询任务个数
    @GetMapping("/taskNumb")
    public Result GetTaskNumb() {
        List<MyTask> allTasks = myTaskService.list();
        Integer taskNumb = allTasks.size();
        return Result.success(taskNumb);
    }



    @GetMapping("/result/{id}")
    public Result result(@PathVariable int id) throws JsonProcessingException {
        MyTask myTask = myTaskService.getlistbyId(id);
        // TaskRequest request = new TaskRequest();
        System.out.println(myTask);
        return Result.success(myTask);

    }

    @GetMapping("/result/pred/{id}")
    public Result result_pred(@PathVariable int id) throws JsonProcessingException {
        Task task = taskService.getlistbyId(id);

        System.out.println(task);
        // 去除首尾的方括号并按逗号分割字符串
        String[] elementsArray = task.getFeature().substring(1, task.getFeature().length() - 1).replace("\"", "").split(",");
        // 创建列表并添加元素
        List<String> elementList = new ArrayList<>(Arrays.asList(elementsArray));
        System.out.println(elementsArray);

        System.out.println(Result.success(elementList));
        return Result.success(elementList);

    }

//    @GetMapping("/delete/{id}")
//    public Result deleteById(@PathVariable int id){
//        taskService.deleteTask(id);
//        return Result.success(taskService.getTaskList());
//    }


    @GetMapping("/delete/{id}")
    public Result deleteById(HttpServletRequest request, @PathVariable int id){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);
        System.out.println("token:" + token);
        System.out.println("curId:" + curId);
        System.out.println("curUser:" + curUser);

        MyTask task = myTaskService.getlistbyId(id);
        myTaskService.deleteTaskById(id);

        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，删除一个任务。被删除任务的任务名和模型名：：" + task.getTaskname() + ", " + task.getModelname());
        return Result.success(200, "删除给", myTaskService.getTaskList());
    }


    // 统计任务频次
    @GetMapping("/GetAllTaskFrequency")
    public Map<String, Integer> getAllTaskFrequency() {
        List<MyTask> allTasks = myTaskService.list();
        Map<String, Integer> result = new HashMap<String, Integer>();
        // 遍历列表，统计每个元素的频次
        for (int i = 0; i < allTasks.size(); i++) {
            String temp = allTasks.get(i).getModelname();
            result.put(temp, result.getOrDefault(temp,0) + 1);
        }
//        // 打印结果
//        for (Map.Entry<String, Integer> entry : result.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
        return result;
    }



    @PostMapping("/save")
    public Result insert(
            @RequestBody(required=false)  String arg,
            HttpServletRequest request
    ){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);


        JSONObject object = JSONObject.parseObject(arg);

        String taskname = object.getString("taskName");
        String leader = object.getString("leader");
        String participant = object.getString("participant");
        String disease = object.getString("disease");
        String dataset = object.getString("dataset");
        String feature = object.getString("feature");
        String targetcolumn = object.getString("targetcolumn");
        String model = object.getString("models");
        String uid = object.getString("uid");
        Timestamp createtime = new Timestamp(new Date().getTime());
        String tips = "";

        // 模型分解
        String jsonString = model;
        ObjectMapper objectMapper = new ObjectMapper();
        Object rootNode = new Object();
        try {
            rootNode = objectMapper.readTree(jsonString);
//            System.out.println(rootNode.toPrettyString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonNode taskmodel = (JsonNode) rootNode;



        // 有几个算法就添加几条任务
        for (int i = 0; i < taskmodel.size(); i++) {
            JsonNode alg = taskmodel.get(i);
            String modelname = String.valueOf(alg.get("name"));
            modelname = modelname.substring(1, modelname.length() - 1);
            String modeltype = String.valueOf(alg.get("model_type"));
            modeltype = modeltype.substring(1, modeltype.length() - 1);
            String modelparams = String.valueOf(alg.get("params"));
            modelparams = modelparams.substring(1, modelparams.length() - 1);

            // 结果解析
            JsonNode modelres = alg.get("res");

            String totallosses = String.valueOf(modelres.get("total_losses"));
            String totalrewards = String.valueOf(modelres.get("total_rewards"));
            String precision = String.valueOf(modelres.get("precision"));
            String recall = String.valueOf(modelres.get("recall"));
            String FN = String.valueOf(modelres.get("FN"));
            String accuracy = String.valueOf(modelres.get("accuracy"));
            String FP = String.valueOf(modelres.get("FP"));
            String TN = String.valueOf(modelres.get("TN"));
            String f1 = String.valueOf(modelres.get("f1"));
            String TP = String.valueOf(modelres.get("TP"));
            String avgshapvalue = String.valueOf(modelres.get("avg_shapvalue"));
            if (!totallosses.equals("null")) {
                totallosses = totallosses.substring(1,totallosses.length() - 1);
            }else {
                totallosses = "";
            }
            if (!totalrewards.equals("null")) {
                totalrewards = totalrewards.substring(1,totalrewards.length() - 1);
            }else {
                totalrewards = "";
            }
            if (!precision.equals("null")) {
                precision = precision.substring(1,precision.length() - 1);
            }else {
                precision = "";
            }
            if (!recall.equals("null")) {
                recall = recall.substring(1,recall.length() - 1);
            }else {
                recall = "";
            }
            if (!FN.equals("null")) {
                FN = FN.substring(1,FN.length() - 1);
            }else {
                FN = "";
            }
            if (!accuracy.equals("null")) {
                accuracy = accuracy.substring(1,accuracy.length() - 1);
            }else {
                accuracy = "";
            }
            if (!FP.equals("null")) {
                FP = FP.substring(1,FP.length() - 1);
            }else {
                FP = "";
            }
            if (!TN.equals("null")) {
                TN = TN.substring(1,TN.length() - 1);
            }else {
                TN = "";
            }
            if (!f1.equals("null")) {
                f1 = f1.substring(1,f1.length() - 1);
            }else {
                f1 = "";
            }
            if (!TP.equals("null")) {
                TP = TP.substring(1,TP.length() - 1);
            }else {
                TP = "";
            }
            if (!avgshapvalue.equals("null")) {
                avgshapvalue = avgshapvalue.substring(1,avgshapvalue.length() - 1);
            }else {
                avgshapvalue = "";
            }

            MyTask task = new MyTask();
            task.setTaskname(taskname);
            task.setLeader(leader);
            task.setParticipant(participant);
            task.setDisease(disease);
            task.setDataset(dataset);
            task.setFeature(feature);
            task.setTargetcolumn(targetcolumn);
            task.setUid(uid);
            task.setCreatetime(createtime);
            task.setTips(tips);
            task.setModelname(modelname);
            task.setModelparams(modelparams);
            task.setModeltype(modeltype);
            task.setTotallosses(totallosses);
            task.setPrecision(precision);
            task.setRecall(recall);
            task.setFn(FN);
            task.setAccuracy(accuracy);
            task.setFp(FP);
            task.setTn(TN);
            task.setTotalrewards(totalrewards);
            task.setF1(f1);
            task.setTp(TP);
            task.setAvgshapvalue(avgshapvalue);
            myTaskService.save(task);
            logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，添加一个任务。添加任务的任务名和模型名：：" + task.getTaskname() + ", " + task.getModelname());
        }
        return Result.success(200,"创建任务成功");
    }



    //用以首页统计使用
    @GetMapping("/GetTaskNearlySevenDays")
    public TaskStaticVo getTaskNearlySevenDays() {
        List<DateCount> tasks = myTaskService.getTaskNearlySevenDays();

        TaskStaticVo result = new TaskStaticVo();
        List<String> date = new ArrayList<String>();
        List<Integer> number = new ArrayList<Integer>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 定义日期格式
        for (DateCount task : tasks) {
            String dateKey = sdf.format(task.getDate()); // 将Timestamp转换为格式化的字符串
            date.add(dateKey);
            number.add(task.getCount());
        }

        result.setDate(date);
        result.setNumber(number);

        return result;
    }

}
