package com.cqupt.software_10.controller;

import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.entity.AdminDataManage;
import com.cqupt.software_10.entity.LogEntity;
import com.cqupt.software_10.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    LogService logService;

    @GetMapping("/getAllLogs")
    public Result getAllLogs(){
        List<LogEntity> allLogs = logService.getAllLogs();
        return Result.success(allLogs);
    }

    @GetMapping("/searchLog")
    public Result selectDataByName(
            @RequestParam("searchType") String searchType,
            @RequestParam("name") String name
    ){
        List<LogEntity> logs = logService.selectDataByName(searchType, name);
        Map<String, Object> ret =  new HashMap<>();
        ret.put("list", logs);
        ret.put("total", logs.size());
        return Result.success("200",ret);
    }
}
