package com.cqupt.software_10.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.entity.Model;

import java.util.List;
public interface ModelService extends IService<Model> {
    List<String> getModelNames();
}
