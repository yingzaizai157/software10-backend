package com.cqupt.software_10.service;

import com.cqupt.software_10.entity.request.RuntimeBusCreateRequest;
import com.cqupt.software_10.entity.response.RuntimeBusServiceResponse;

public interface RuntimeBusService {
    RuntimeBusServiceResponse submitBus(String tableName) throws Exception;
    RuntimeBusServiceResponse submitRLModel() throws Exception;
    RuntimeBusServiceResponse submitRLModel2() throws Exception;
    RuntimeBusServiceResponse submitDQN(String rate) throws Exception;

    RuntimeBusServiceResponse submitDQNPred(String model) throws Exception;
}
