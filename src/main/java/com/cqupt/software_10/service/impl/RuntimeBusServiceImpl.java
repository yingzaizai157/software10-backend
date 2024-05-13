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

    @Value("${gorit.file.py.XGB}")
    private String XGBAlg;

    @Value("${gorit.file.py.xgbc_rl}")
    private String xgbc_rl;

    @Value("${gorit.file.py.rlv3_2}")
    private String rlv3_2;

    @Value("${gorit.file.py.SVM}")
    private String svm;

    @Value("${gorit.file.py.KNN}")
    private String knn;

    @Value("${gorit.file.py.DQN}")
    private String dqn1;

    @Value("${gorit.file.py.QLearning}")
    private String qLearning;

    @Value("${gorit.file.py.getLackInfos}")
    private String getLackInfos;

    @Value("${gorit.file.py.RF}")
    private String RFAlg;

    @Value("${gorit.file.py.use_model}")
    private String use_model;

    @Value("${gorit.file.py.RunQLearning}")
    private String runqLearning;

    @Value("${gorit.file.py.RUNDQN}")
    private String runDQN;

    @Value("${gorit.file.py.RUNKNN}")
    private String runKNN;

    @Value("${gorit.file.py.RUNSVM}")
    private String runSVM;

    @Override
    public RuntimeBusServiceResponse submitBus(String tableName, List<String> cols, List<String> labels, String alg, String mode) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--table_name=" + tableName);
        args.add("--mode=" + mode);

        String temp = "--cols=";
        for (int i = 0; i < cols.size(); i++) {
            if (i == 0) {
                temp = temp + cols.get(i);
            }else {
                temp = temp + "," + cols.get(i);
            }
        }
        args.add(temp);

        temp = "--labels=";
        for (int i = 0; i < labels.size(); i++) {
            if (i == 0) {
                temp = temp + labels.get(i);
            }else {
                temp = temp + "," + labels.get(i);
            }
        }
        args.add(temp);
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        if (alg.equals("RF")) {
            runtimeTaskRequest.setPyPath(RFAlg);
        }else if (alg.equals("XGB")) {
            runtimeTaskRequest.setPyPath(XGBAlg);
        }

        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse=runtimeTaskService.submitTask(runtimeTaskRequest);
        // response.setTaskCreateTime(new Timestamp(taskResponse.getTaskFinishTime()));
        response.setRes(taskResponse.getRes());
        return response;
    }

    @Override
    public RuntimeBusServiceResponse submitGetLackInfos(String tableName, String modename) throws Exception {

        List<String> args = new ArrayList<>();
        args.add("--table_name="+tableName);
        //args.add("--modename="+modename);
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(getLackInfos);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse = runtimeTaskService.submitTask(runtimeTaskRequest);
        // response.setTaskCreateTime(new Timestamp(taskResponse.getTaskFinishTime()));
        response.setRes(taskResponse.getRes());
        return response;
    }

    @Override
    public RuntimeBusServiceResponse submitRFAlg(String tableName) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--table_name=" + tableName);
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(RFAlg);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse = runtimeTaskService.submitTask(runtimeTaskRequest);
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

    @Override
    public RuntimeBusServiceResponse submitSVM(String kernel, String random_state, String cv, String modelName, String table_name, List<String> cols, List<String> labels) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--kernel=" + kernel);
        args.add("--random_state=" + Integer.parseInt(random_state));
//        args.add("--paramRange=" + Integer.parseInt(paramRange));
        args.add("--cv=" + Integer.parseInt(cv));
        args.add("--modelName=" + modelName);
        args.add("--table_name=" + table_name);

        String temp = "--cols=";
        for (int i = 0; i < cols.size(); i++) {
            if (i == 0) {
                temp = temp + cols.get(i);
            }else {
                temp = temp + "," + cols.get(i);
            }
        }
        args.add(temp);

        temp = "--labels=";
        for (int i = 0; i < labels.size(); i++) {
            if (i == 0) {
                temp = temp + labels.get(i);
            }else {
                temp = temp + "," + labels.get(i);
            }
        }
        args.add(temp);
        System.out.println("args:  " + args);




        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(svm);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse=runtimeTaskService.submitRLModelTask(runtimeTaskRequest);
        // response.setTaskCreateTime(new Timestamp(taskResponse.getTaskFinishTime()));
        response.setRes(taskResponse.getRes());
        return response;
    }

    @Override
    public RuntimeBusServiceResponse submitKNN(String K,String random_state, String cv, String modelName, String table_name, List<String> cols, List<String> labels) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--K=" + K);
        args.add("--random_state=" + random_state);
        args.add("--cv=" + Integer.parseInt(cv));
        args.add("--modelName=" + modelName);
        args.add("--table_name=" + table_name);

        String temp = "--cols=";
        for (int i = 0; i < cols.size(); i++) {
            if (i == 0) {
                temp = temp + cols.get(i);
            }else {
                temp = temp + "," + cols.get(i);
            }
        }
        args.add(temp);

        temp = "--labels=";
        for (int i = 0; i < labels.size(); i++) {
            if (i == 0) {
                temp = temp + labels.get(i);
            }else {
                temp = temp + "," + labels.get(i);
            }
        }
        args.add(temp);
        System.out.println("args:  " + args);




        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(knn);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse = runtimeTaskService.submitRLModelTask(runtimeTaskRequest);
        // response.setTaskCreateTime(new Timestamp(taskResponse.getTaskFinishTime()));
        response.setRes(taskResponse.getRes());
        return response;
    }

    public RuntimeBusServiceResponse submitDQN(String reward, String epoch, String gamma, String learning_rate, String modelName, String table_name, List<String> cols, List<String> labels, List<String> features_label, List<String> features_doctorRate) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--reward=" + Double.parseDouble(reward));
        args.add("--epoch=" + (int) Double.parseDouble(epoch));
        args.add("--gamma=" + Double.parseDouble(gamma));
        args.add("--learning_rate=" + Double.parseDouble(learning_rate));
        args.add("--modelName=" + modelName);
        args.add("--table_name=" + table_name);

        String temp = "--cols=";
        for (int i = 0; i < cols.size(); i++) {
            if (i == 0) {
                temp = temp + cols.get(i);
            }else {
                temp = temp + "," + cols.get(i);
            }
        }
        args.add(temp);

        temp = "--labels=";
        for (int i = 0; i < labels.size(); i++) {
            if (i == 0) {
                temp = temp + labels.get(i);
            }else {
                temp = temp + "," + labels.get(i);
            }
        }
        args.add(temp);

        temp = "--features_label=";
        for (int i = 0; i < features_label.size(); i++) {
            if (i == 0) {
                temp = temp + features_label.get(i);
            }else {
                temp = temp + "," + features_label.get(i);
            }
        }
        args.add(temp);

        temp = "--features_doctorRate=";
        for (int i = 0; i < features_doctorRate.size(); i++) {
            if (i == 0) {
                temp = temp + String.valueOf(features_doctorRate.get(i));
            }else {
                temp = temp + "," + String.valueOf(features_doctorRate.get(i));
            }
        }
        args.add(temp);
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
    public RuntimeBusServiceResponse submitQLearning(String lr, String epsilon, String gamma, String declay, String episodes, String modelName, String table_name, List<String> cols, List<String> labels, List<String> features_label, List<String> features_doctorRate) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--lr=" + Double.parseDouble(lr));
        args.add("--epsilon=" + (int) Double.parseDouble(epsilon));
        args.add("--gamma=" + Double.parseDouble(gamma));
        args.add("--declay=" + Double.parseDouble(declay));
        args.add("--episodes=" + Double.parseDouble(episodes));
        args.add("--modelName=" + modelName);
        args.add("--table_name=" + table_name);

        String temp = "--cols=";
        for (int i = 0; i < cols.size(); i++) {
            if (i == 0) {
                temp = temp + cols.get(i);
            }else {
                temp = temp + "," + cols.get(i);
            }
        }
        args.add(temp);

        temp = "--labels=";
        for (int i = 0; i < labels.size(); i++) {
            if (i == 0) {
                temp = temp + labels.get(i);
            }else {
                temp = temp + "," + labels.get(i);
            }
        }
        args.add(temp);

        temp = "--features_label=";
        for (int i = 0; i < features_label.size(); i++) {
            if (i == 0) {
                temp = temp + features_label.get(i);
            }else {
                temp = temp + "," + features_label.get(i);
            }
        }
        args.add(temp);

        temp = "--features_doctorRate=";
        for (int i = 0; i < features_doctorRate.size(); i++) {
            if (i == 0) {
                temp = temp + String.valueOf(features_doctorRate.get(i));
            }else {
                temp = temp + "," + String.valueOf(features_doctorRate.get(i));
            }
        }
        args.add(temp);
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(qLearning);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse=runtimeTaskService.submitRLModelTask(runtimeTaskRequest);
        // response.setTaskCreateTime(new Timestamp(taskResponse.getTaskFinishTime()));
        response.setRes(taskResponse.getRes());
        return response;
    }

    @Override
    public RuntimeBusServiceResponse runqLearning(String modelname, String taskname, List<String> onedata) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--modelname=" + modelname);
        args.add("--taskname=" + taskname);

        String temp = "--onedata=";
        for (int i = 0; i < onedata.size(); i++) {
            if (i == 0) {
                temp = temp + onedata.get(i);
            }else {
                temp = temp + "," + onedata.get(i);
            }
        }
        args.add(temp);
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(runqLearning);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse=runtimeTaskService.submitRLModelTask(runtimeTaskRequest);
        // response.setTaskCreateTime(new Timestamp(taskResponse.getTaskFinishTime()));
        response.setRes(taskResponse.getRes());
        return response;
    }

    @Override
    public RuntimeBusServiceResponse rundqn(String modelname, String taskname, List<String> onedata) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--modelname=" + modelname);
        args.add("--taskname=" + taskname);

        String temp = "--onedata=";
        for (int i = 0; i < onedata.size(); i++) {
            if (i == 0) {
                temp = temp + onedata.get(i);
            }else {
                temp = temp + "," + onedata.get(i);
            }
        }
        args.add(temp);
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(runDQN);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse=runtimeTaskService.submitRLModelTask(runtimeTaskRequest);
        // response.setTaskCreateTime(new Timestamp(taskResponse.getTaskFinishTime()));
        response.setRes(taskResponse.getRes());
        return response;
    }

    @Override
    public RuntimeBusServiceResponse runknn(String modelname, String taskname, List<String> onedata) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--modelname=" + modelname);
        args.add("--taskname=" + taskname);

        String temp = "--onedata=";
        for (int i = 0; i < onedata.size(); i++) {
            if (i == 0) {
                temp = temp + onedata.get(i);
            }else {
                temp = temp + "," + onedata.get(i);
            }
        }
        args.add(temp);
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(runKNN);
        runtimeTaskRequest.setArgs(args);

        System.out.println("Python脚本路径：" + runtimeTaskRequest.getPyPath());

        RuntimeTaskResponse taskResponse=runtimeTaskService.submitRLModelTask(runtimeTaskRequest);
        // response.setTaskCreateTime(new Timestamp(taskResponse.getTaskFinishTime()));
        response.setRes(taskResponse.getRes());
        return response;
    }

    @Override
    public RuntimeBusServiceResponse runsvm(String modelname, String taskname, List<String> onedata) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("--modelname=" + modelname);
        args.add("--taskname=" + taskname);

        String temp = "--onedata=";
        for (int i = 0; i < onedata.size(); i++) {
            if (i == 0) {
                temp = temp + onedata.get(i);
            }else {
                temp = temp + "," + onedata.get(i);
            }
        }
        args.add(temp);
        System.out.println("args:  " + args);

        RuntimeBusServiceResponse response = new RuntimeBusServiceResponse();
        RuntimeTaskRequest runtimeTaskRequest = new RuntimeTaskRequest();

        runtimeTaskRequest.setPyPath(runSVM);
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
