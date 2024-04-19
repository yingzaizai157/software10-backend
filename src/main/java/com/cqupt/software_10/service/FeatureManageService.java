package com.cqupt.software_10.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.entity.FeatureEntity;
import com.cqupt.software_10.vo.FeatureListVo;
import com.cqupt.software_10.vo.FeatureVo;

import java.util.List;
// TODO 公共模块新增类
public interface FeatureManageService extends IService<FeatureEntity> {
    List<FeatureVo> getFeatureList(String belongType);

    void insertFeatures(FeatureListVo featureListVo);
}
