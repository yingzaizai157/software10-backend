package com.cqupt.software_10.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.dao.CategoryMapper;
import com.cqupt.software_10.dao.TableDescribeMapper;
import com.cqupt.software_10.entity.Category2Entity;
import com.cqupt.software_10.entity.CategoryEntity;
import com.cqupt.software_10.entity.TableDescribeEntity;
import com.cqupt.software_10.entity.user.User;
import com.cqupt.software_10.mapper.user.UserMapper;
import com.cqupt.software_10.service.Category2Service;
import com.cqupt.software_10.service.CategoryService;
import com.cqupt.software_10.service.LogService;
import com.cqupt.software_10.service.user.UserService;
import com.cqupt.software_10.util.SecurityUtil;
import com.cqupt.software_10.vo.AddDiseaseVo;
import com.cqupt.software_10.vo.DeleteDiseaseVo;
import com.cqupt.software_10.vo.UpdateDiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
    TableDescribeMapper tableDescribeMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    LogService logService;

    @Autowired
    UserService userService;


    // 获取目录
    @GetMapping("/category")
    public Result<List<CategoryEntity>> getCatgory(@RequestParam String uid){
        List<CategoryEntity> list = categoryService.getCategory(uid);
//        System.out.println(JSON.toJSONString(list));
        return Result.success("200",list);
    }

    @GetMapping("/Taskcategory")
    public Result<List<CategoryEntity>> getCatgory(){
        List<CategoryEntity> list = categoryService.getTaskCategory();
//        System.out.println(JSON.toJSONString(list));
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
    public Result addDisease(@RequestBody CategoryEntity categoryNode, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);


        System.out.println("参数为："+ JSON.toJSONString(categoryNode));
        categoryService.save(categoryNode);

        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，新增目录，" + categoryNode.getLabel());
        return Result.success(200,"新增目录成功");
    }


    // 删除一个目录
    @Transactional
    @GetMapping("/category/remove")
    public Result removeCate(CategoryEntity categoryEntity, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);


        System.out.println("要删除的目录为："+JSON.toJSONString(categoryEntity));
        if(categoryEntity.getIsLeafs()==0){
            categoryService.removeNode(categoryEntity.getId());
        }
        else {
            categoryService.removeNode(categoryEntity.getId(),categoryEntity.getLabel());
            TableDescribeEntity tableDescribeEntity = tableDescribeMapper.selectOne(new QueryWrapper<TableDescribeEntity>().eq("table_id",categoryEntity.getId()));
            if(tableDescribeEntity.getTableSize()!=0){
                userMapper.recoveryUpdateUserColumnById(tableDescribeEntity.getUid(),new Double(tableDescribeEntity.getTableSize()));
            }
            tableDescribeMapper.delete(new QueryWrapper<TableDescribeEntity>().eq("table_id",categoryEntity.getId()));
//            tTableMapper.delete(new QueryWrapper<tTable>().eq("table_name",categoryEntity.getLabel()));

        }


        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，删除目录，" + categoryEntity.getLabel());
        return Result.success(200,"删除成功");
    }


    /**
     * 新增检查疾病名是否存在
     */
    @GetMapping("/category/checkDiseaseName/{diseaseName}")
    public Result checkDiseaseName(@PathVariable String diseaseName){
        QueryWrapper<CategoryEntity> queryWrapper = Wrappers.query();
        queryWrapper.eq("label", diseaseName)
                .eq("is_delete", 0)
                .isNull("status");
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




//    @PostMapping("/category/deleteCategory")
//    public Result deleteCategory(@RequestBody DeleteDiseaseVo deleteDiseaseVo){
//        StringJoiner joiner = new StringJoiner(",");
//        for (String str : deleteDiseaseVo.getDeleteNames()) {
//            joiner.add(str);
//        }
////        UserLog userLog = new UserLog(null,Integer.parseInt(deleteDiseaseVo.getUid()),new Date(),"删除病种："+joiner.toString(),deleteDiseaseVo.getUsername());
////        userLogService.save(userLog);
//        categoryService.removeCategorys(deleteDiseaseVo.getDeleteIds());
//        return Result.success("删除成功");
//    }
















    @GetMapping("/addParentDisease")
    public Result addParentDisease(@RequestParam("diseaseName") String diseaseName, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);

        System.out.println("name:"+diseaseName);
        categoryService.addParentDisease(diseaseName);

        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，添加疾病名称，" + diseaseName);
        return Result.success("200",null);
    }

    @GetMapping("/changeStatus")
    public Result changeStatus(CategoryEntity categoryEntity, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);

        categoryService.changeStatus(categoryEntity);

        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，修改树情况，" + categoryEntity);
        return Result.success(200,"修改成功",null);
    }


    @GetMapping("/inspectionOfIsNotCommon")
    public Result inspectionOfIsNotCommon(@RequestParam("newname") String name){
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        wrapper.eq("is_common", 0);
        List<CategoryEntity> list = categoryService.list(wrapper);
        List<String>  nameList = new ArrayList<>();

        for (CategoryEntity temp :list) {
            nameList.add(temp.getLabel());
        }
        boolean flag = true;
        for (String  tempName : nameList) {
            if(tempName.equals(name)) {
                flag = false;
                break;
            }
        }
        return Result.success("200",flag); // 判断文件名是否重复
    }

    @GetMapping("/inspectionOfIsCommon")
    public Result inspectionOfIsCommon(@RequestParam("newname") String name){
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        wrapper.eq("is_common", 1);
        List<CategoryEntity> list = categoryService.list(wrapper);
        List<String>  nameList = new ArrayList<>();

        for (CategoryEntity temp :list) {
            nameList.add(temp.getLabel());
        }
        boolean flag = true;
        for (String  tempName : nameList) {
            if(tempName.equals(name)) {
                flag = false;
                break;
            }
        }
        return Result.success("200",flag); // 判断文件名是否重复
    }


    //    zongqing新增疾病管理模块
    @GetMapping("/category/getAllDisease")
    public Result<List<CategoryEntity>> getAllDisease(){
        List<CategoryEntity> list = categoryService.getAllDisease();
        System.out.println(JSON.toJSONString(list));
        return Result.success("200",list);
    }
    @PostMapping("/category/addCategory")
    public Result addCategory(@RequestBody AddDiseaseVo addDiseaseVo, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);

        int result = categoryService.addCategory(addDiseaseVo);

        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，添加树枝：" + addDiseaseVo);
        return Result.success("200",result);
    }
    @PostMapping("/category/updateCategory")
    public Result updateCategory(@RequestBody UpdateDiseaseVo updateDiseaseVo, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);


        Result result = categoryService.updateCategory(updateDiseaseVo);

        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，更新树枝：" + updateDiseaseVo);
        return result;

    }
//    @PostMapping("/category/deleteCategory")
//    public Result deleteCategory(@RequestBody List<String> deleteIds, HttpServletRequest request){
//        String token = request.getHeader("Authorization");
//        String curId = SecurityUtil.getUserIdFromToken(token);
//        User curUser = userService.getUserById(curId);
//
//        System.out.println("删除");
//        System.out.println(deleteIds);
//        categoryService.removeCategorys(deleteIds);
//
//        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，删除树枝：" + deleteIds);
//        return Result.success("删除成功");
//    }


    @PostMapping("/category/deleteCategory")
    public Result deleteCategory(@RequestBody DeleteDiseaseVo deleteDiseaseVo, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);


        StringJoiner joiner = new StringJoiner(",");
        for (String str : deleteDiseaseVo.getDeleteNames()) {
            joiner.add(str);
        }
        categoryService.removeCategorys(deleteDiseaseVo.getDeleteIds());


        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，删除树枝：" + deleteDiseaseVo);
        return Result.success("删除成功");
    }

}
