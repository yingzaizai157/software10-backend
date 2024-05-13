package com.cqupt.software_10.service;

import com.cqupt.software_10.entity.request.RuntimeBusCreateRequest;
import com.cqupt.software_10.entity.response.RuntimeBusServiceResponse;

import java.util.List;

public interface RuntimeBusService {
    RuntimeBusServiceResponse submitBus(String tableName, List<String> cols, List<String> labels, String alg, String mode) throws Exception;

    RuntimeBusServiceResponse submitGetLackInfos(String tableName, String modename) throws Exception;

    RuntimeBusServiceResponse submitRFAlg(String tableName) throws Exception;

//    RuntimeBusServiceResponse submitRFAlg(String tableName) throws Exception;

    RuntimeBusServiceResponse submitRLModel() throws Exception;
    RuntimeBusServiceResponse submitRLModel2() throws Exception;

//    RuntimeBusServiceResponse submitSVM(String kernel, String random_state, String paramRange, String cv) throws Exception;
    RuntimeBusServiceResponse submitSVM(String kernel, String random_state, String cv, String modelName, String table_name, List<String> cols, List<String> labels) throws Exception;


    RuntimeBusServiceResponse submitKNN(String paramRange, String random_state, String cv, String modelName, String table_name, List<String> cols, List<String> labels) throws Exception;

    RuntimeBusServiceResponse submitDQN(String reward, String epoch, String gamma, String learning_rate, String modelName, String table_name, List<String> cols, List<String> labels, List<String> features_label, List<String> features_doctorRate) throws Exception;

    RuntimeBusServiceResponse submitQLearning(String lr, String epsilon, String gamma, String declay, String episodes, String modelName, String table_name, List<String> cols, List<String> labels, List<String> features_label, List<String> features_doctorRate) throws Exception;

    RuntimeBusServiceResponse runqLearning(String modelname, String taskname, List<String> onedata) throws Exception;

    RuntimeBusServiceResponse rundqn(String modelname, String taskname, List<String> onedata) throws Exception;

    RuntimeBusServiceResponse runknn(String modelname, String taskname, List<String> onedata) throws Exception;

    RuntimeBusServiceResponse runsvm(String modelname, String taskname, List<String> onedata) throws Exception;

    RuntimeBusServiceResponse submitDQNPred(String model) throws Exception;
}
