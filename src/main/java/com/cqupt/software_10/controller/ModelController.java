package com.cqupt.software_10.controller;


import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Model")
public class ModelController {
    @Autowired
    private ModelService modelService;

    @GetMapping("/all")
    public Result getModelNames(){
        return Result.success(
                200,"获取统计信息成功",modelService.getModelNames());
    }
}
