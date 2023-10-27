package com.cqupt.software_10.service;

import com.cqupt.software_10.entity.request.RuntimeTaskRequest;
import com.cqupt.software_10.entity.response.RuntimeTaskResponse;

public interface RuntimeTaskService {
    RuntimeTaskResponse submitTask(RuntimeTaskRequest request) throws Exception;
    RuntimeTaskResponse submitRLModelTask(RuntimeTaskRequest request) throws Exception;
    RuntimeTaskResponse submitRLModelTask2(RuntimeTaskRequest request) throws Exception;
}

