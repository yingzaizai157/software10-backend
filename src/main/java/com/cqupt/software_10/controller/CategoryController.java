package com.cqupt.software_10.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.dao.CategoryMapper;
import com.cqupt.software_10.entity.Category2Entity;
import com.cqupt.software_10.entity.CategoryEntity;
import com.cqupt.software_10.service.Category2Service;
import com.cqupt.software_10.service.CategoryService;
import com.cqupt.software_10.vo.AddDiseaseVo;
import com.cqupt.software_10.vo.DeleteDiseaseVo;
import com.cqupt.software_10.vo.UpdateDiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

// TODO 公共模块新增类
@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    Category2Service category2Service;
    @Autowired
    CategoryMapper categoryMapper;


    // 获取目录
    @GetMapping("/category")
    public Result<List<CategoryEntity>> getCatgory(){
        List<CategoryEntity> list = categoryService.getCategory();
        System.out.println(JSON.toJSONString(list));
        return Result.success("200",list);
    }


    // 字段管理获取字段
    @GetMapping("/category2")
    public Result<List<Category2Entity>> getCatgory2(){
        List<Category2Entity> list = category2Service.getCategory2();
        return Result.success("200",list);
    }

    // 创建一种新的疾病
    @PostMapping("/addDisease")
    public Result addDisease(@RequestBody CategoryEntity categoryNode){
        System.out.println("参数为："+ JSON.toJSONString(categoryNode));
        categoryService.save(categoryNode);
        return Result.success(200,"新增目录成功");
    }


    // 删除一个目录
    @GetMapping("/category/remove")
    public Result removeCate(CategoryEntity categoryEntity){
        System.out.println("要删除的目录为："+JSON.toJSONString(categoryEntity));
        categoryService.removeNode(categoryEntity.getId());
        return Result.success(200,"删除成功");
    }


    @GetMapping("/category/getAllDisease")
    public Result<List<CategoryEntity>> getAllDisease(){
        List<CategoryEntity> list = categoryService.getAllDisease();
        System.out.println(JSON.toJSONString(list));
        return Result.success("200",list);
    }
    /**
     * 新增检查疾病名是否存在
     */
    @GetMapping("/category/checkDiseaseName/{diseaseName}")
    public Result checkDiseaseName(@PathVariable String diseaseName){
        QueryWrapper<CategoryEntity> queryWrapper = Wrappers.query();
        queryWrapper.eq("label", diseaseName)
                .eq("is_delete", 0);
        CategoryEntity category = categoryMapper.selectOne(queryWrapper);
        return category==null?Result.success("200","病种名可用"):Result.fail("400","病种名已存在");
    }


    @GetMapping("/getParentLabel/{pid}")
    public Result getParentLabel(@PathVariable String pid){
        String label = categoryService.getLabelByPid(pid);
        return Result.success(200,label);
    }


    // TODO 获取专病数据集个数
    @GetMapping("/getSpDiseaseNum")
    public Result getSpDiseaseNum(){
        List<CategoryEntity> list = categoryService.getSpDisease();
        return Result.success("200",list.size());
    }

    // TODO 获取公病数据集个数
    @GetMapping("/getComDiseaseNum")
    public Result getComDiseaseNum(){
        List<CategoryEntity> list = categoryService.getComDisease();
        return Result.success("200",list.size());
    }


    @GetMapping("/addParentDisease")
    public Result addParentDisease(@RequestParam("diseaseName") String diseaseName){
        System.out.println("name:"+diseaseName);
        categoryService.addParentDisease(diseaseName);
        return Result.success(200,"添加成功");
    }


    @PostMapping("/category/addCategory")
    public Result addCategory(@RequestBody AddDiseaseVo addDiseaseVo){
        if(categoryService.addCategory(addDiseaseVo)>0){
//            UserLog userLog = new UserLog(null,Integer.parseInt(addDiseaseVo.getUid()),new Date(),"添加病种"+addDiseaseVo.getFirstDisease(),addDiseaseVo.getUsername());
//            userLogService.save(userLog);
            return Result.success("添加病种成功");
        }else{
//            UserLog userLog = new UserLog(null,Integer.parseInt(addDiseaseVo.getUid()),new Date(),"添加病种失败",addDiseaseVo.getUsername());
//            userLogService.save(userLog);
            return Result.fail("添加病种失败");
        }
    }
    @PostMapping("/category/updateCategory")
    public Result updateCategory(@RequestBody UpdateDiseaseVo updateDiseaseVo){
//        UserLog userLog = new UserLog(null,Integer.parseInt(updateDiseaseVo.getUid()),new Date(),"修改病种"+updateDiseaseVo.getOldName()+"为"+updateDiseaseVo.getDiseaseName(),updateDiseaseVo.getUsername());
//        userLogService.save(userLog);
        return categoryService.updateCategory(updateDiseaseVo);
    }
    @PostMapping("/category/deleteCategory")
    public Result deleteCategory(@RequestBody DeleteDiseaseVo deleteDiseaseVo){
        StringJoiner joiner = new StringJoiner(",");
        for (String str : deleteDiseaseVo.getDeleteNames()) {
            joiner.add(str);
        }
//        UserLog userLog = new UserLog(null,Integer.parseInt(deleteDiseaseVo.getUid()),new Date(),"删除病种："+joiner.toString(),deleteDiseaseVo.getUsername());
//        userLogService.save(userLog);
        categoryService.removeCategorys(deleteDiseaseVo.getDeleteIds());
        return Result.success("删除成功");
    }

}
