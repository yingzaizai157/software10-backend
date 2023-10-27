package com.cqupt.software_10.service.impl;

import com.cqupt.software_10.entity.request.RuntimeTaskRequest;
import com.cqupt.software_10.entity.response.RuntimeBusServiceResponse;
import com.cqupt.software_10.entity.response.RuntimeTaskResponse;
import com.cqupt.software_10.service.RuntimeBusService;
import com.cqupt.software_10.service.RuntimeTaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class RuntimeBusServiceImpl implements RuntimeBusService {
    @Resource
    private RuntimeTaskService runtimeTaskService;

    @Value("${gorit.file.py.diabetes}")
    private String diabetes;

    @Value("${gorit.file.py.xgbc_rl}")
    private String xgbc_rl;

    @Value("${gorit.file.py.rlv3_2}")
    private String rlv3_2;

    @Value("${gorit.file.py.dqn1}")
    private String dqn1;

    @Value("${gorit.file.py.use_model}")
    private String use_model;

    @Override
    public RuntimeBusServiceResponse submitBus() throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--pass--");
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(diabetes);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse=runtimeTaskService.submitTask(runtimeTaskRequest);
        // response.setTaskCreateTime(new Timestamp(taskResponse.getTaskFinishTime()));
        response.setRes(taskResponse.getRes());
        return response;
    }

    public RuntimeBusServiceResponse submitRLModel() throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--rl--");
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(xgbc_rl);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse=runtimeTaskService.submitRLModelTask(runtimeTaskRequest);
        // response.setTaskCreateTime(new Timestamp(taskResponse.getTaskFinishTime()));
        response.setRes(taskResponse.getRes());
        return response;
    }

    @Override
    public RuntimeBusServiceResponse submitRLModel2() throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--rl2--");
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(rlv3_2);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse=runtimeTaskService.submitRLModelTask2(runtimeTaskRequest);
        // response.setTaskCreateTime(new Timestamp(taskResponse.getTaskFinishTime()));
        response.setRes(taskResponse.getRes());
        return response;
    }

    public RuntimeBusServiceResponse submitDQN(String rate) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--rate=" + rate);
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(dqn1);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse=runtimeTaskService.submitRLModelTask(runtimeTaskRequest);
        // response.setTaskCreateTime(new Timestamp(taskResponse.getTaskFinishTime()));
        response.setRes(taskResponse.getRes());
        return response;
    }

    @Override
    public RuntimeBusServiceResponse submitDQNPred(String model) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--model=" + model);
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(use_model);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse=runtimeTaskService.submitRLModelTask(runtimeTaskRequest);
        response.setRes(taskResponse.getRes());
        return response;
    }
}
