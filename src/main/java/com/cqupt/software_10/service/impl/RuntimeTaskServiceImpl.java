package com.cqupt.software_10.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.cqupt.software_10.entity.request.RuntimeTaskRequest;
import com.cqupt.software_10.entity.response.RuntimeTaskResponse;
import com.cqupt.software_10.service.RuntimeTaskService;
import com.cqupt.software_10.tool.PythonRun;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class RuntimeTaskServiceImpl implements RuntimeTaskService {

    @Resource
    private PythonRun pythonRun;

    @Override
    public RuntimeTaskResponse submitTask(RuntimeTaskRequest request) throws Exception {
        RuntimeTaskResponse taskResponse = new RuntimeTaskResponse();
        BeanUtils.copyProperties(request, taskResponse);
        taskResponse.setTaskStartTime(new Date().getTime());
        System.out.println("接收到的PyPath: " + request.getPyPath());
        System.out.println("接收到的Args: " + request.getArgs());
        //任务信息入库
        //提交任务
        taskResponse.setTaskFinishTime(new Date().getTime());
        String a1 = pythonRun.run(request.getPyPath(), request.getArgs());

        System.out.println("a1=" + a1);
        List<String> res = JSONObject.parseArray(pythonRun.run(request.getPyPath(), request.getArgs()), String.class);
        taskResponse.setRes(res);
        System.out.println("res" + res);

        return taskResponse;
    }

    public RuntimeTaskResponse submitRLModelTask(RuntimeTaskRequest request) throws Exception {
        RuntimeTaskResponse taskResponse = new RuntimeTaskResponse();
        BeanUtils.copyProperties(request, taskResponse);
        taskResponse.setTaskStartTime(new Date().getTime());
        System.out.println("接收到的PyPath: " + request.getPyPath());
        System.out.println("接收到的Args: " + request.getArgs());
        //任务信息入库
        //提交任务
        taskResponse.setTaskFinishTime(new Date().getTime());
        String a1 = pythonRun.run(request.getPyPath(), request.getArgs());

        System.out.println("a1=" + a1);
        List<String> res = JSONObject.parseArray(pythonRun.run(request.getPyPath(), request.getArgs()), String.class);
        taskResponse.setRes(res);
        System.out.println("res" + res);
        return taskResponse;
    }

    public RuntimeTaskResponse submitRLModelTask2(RuntimeTaskRequest request) throws Exception {
        RuntimeTaskResponse taskResponse = new RuntimeTaskResponse();
        BeanUtils.copyProperties(request, taskResponse);
        taskResponse.setTaskStartTime(new Date().getTime());
        System.out.println("接收到的PyPath: " + request.getPyPath());
        System.out.println("接收到的Args: " + request.getArgs());
        //任务信息入库
        //提交任务
        taskResponse.setTaskFinishTime(new Date().getTime());
        String a1 = pythonRun.run(request.getPyPath(), request.getArgs());

        System.out.println("a1=" + a1);
        List<String> res = JSONObject.parseArray(pythonRun.run(request.getPyPath(), request.getArgs()), String.class);
        taskResponse.setRes(res);
        System.out.println("res" + res);
        return taskResponse;
    }
}
