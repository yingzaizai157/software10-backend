package com.cqupt.software_10.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.entity.CategoryEntity;
import com.cqupt.software_10.entity.NewCategoryEntity;
import com.cqupt.software_10.vo.AddDiseaseVo;
import com.cqupt.software_10.vo.UpdateDiseaseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
// TODO 公共模块新增类
public interface CategoryService extends IService<CategoryEntity> {
    List<CategoryEntity> getCategory();
    void removeNode(String id);

    List<CategoryEntity> getSpDisease();
    List<CategoryEntity> getComDisease();
    String getLabelByPid(@Param("pid") String pid);

    //    下面方法是管理员端-数据管理新增
//    查看各等级病种
    List<CategoryEntity> getLevel2Label();
    List<CategoryEntity> getLabelsByPid(@Param("pid") String pid);

    public void addParentDisease(String diseaseName);

    //    新增疾病管理模块
    List<CategoryEntity> getAllDisease();
    Result addCategory(AddDiseaseVo addDiseaseVo);
    Result updateCategory(UpdateDiseaseVo updateDiseaseVo);
    void removeCategorys(List<String> deleteIds);
}
