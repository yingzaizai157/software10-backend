package com.cqupt.software_10.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
//import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

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
        System.out.println("list: --"+list);
        return Result.success("200",list);
    }


    @GetMapping("/getAllTableNames")
    public Result getAllTableNames(){
        List<String> res = categoryService.getTableNames();
        return Result.success("200",res);
    }


    // 用在首页的统计信息里面
    @GetMapping("/getDatasetNumber")
    public Result getDatasetNumber(){
        List<CategoryEntity> list = categoryService.getdataset();

        return Result.success("200",list.size());
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
    public Result removeCate(CategoryEntity categoryEntity){
        System.out.println("要删除的目录为："+JSON.toJSONString(categoryEntity));
        if(categoryEntity.getIsLeafs()==0){
            categoryService.removeNode(categoryEntity.getId());
        }
        else {
            categoryService.removeNode(categoryEntity.getId(),categoryEntity.getLabel());
            TableDescribeEntity tableDescribeEntity = tableDescribeMapper.selectOne(new QueryWrapper<TableDescribeEntity>().eq("table_id",categoryEntity.getId()));
            if(tableDescribeEntity.getTableSize()!=0){
                userMapper.recoveryUpdateUserColumnById(tableDescribeEntity.getUid(),tableDescribeEntity.getTableSize());
            }
            tableDescribeMapper.delete(new QueryWrapper<TableDescribeEntity>().eq("table_id",categoryEntity.getId()));
//            tTableMapper.delete(new QueryWrapper<tTable>().eq("table_name",categoryEntity.getLabel()));

        }

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
    @GetMapping("/getDiseaseNum")
    public Result getDiseaseNum(){
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

    // 新增可共享用户列表



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


    // 用以首页统计信息用
    @GetMapping("/category/static")
    public Map<String,Integer> categoryStatic(){
        List<CategoryEntity> categoryEntities = categoryMapper.selectList(null);

        List<CategoryEntity> treeData = categoryEntities.stream().filter((categoryEntity) -> {
            return categoryEntity.getIsLeafs() == 1 && categoryEntity.getIsDelete() == 0 ;
        }).collect(Collectors.toList());

        List<CategoryEntity> disease = categoryEntities.stream().filter((categoryEntity) -> {
            return categoryEntity.getCatLevel() == 2;
        }).collect(Collectors.toList());

        Map<String,Integer> result = new HashMap<String,Integer>();

        for (CategoryEntity categoryEntity : disease) {
            result.put(categoryEntity.getLabel(), 0);
        }

        for (CategoryEntity cate : treeData) {
            while (cate.getCatLevel() != 2) {
                String id = cate.getParentId();
                List<CategoryEntity> temp = categoryMapper.selectList(null).stream().filter((categoryEntity) -> {
                    return categoryEntity.getId().equals(id);
                }).collect(Collectors.toList());
                cate = temp.get(0);
            }
            Integer num = result.get(cate.getLabel());
            result.put(cate.getLabel(), num + 1);
        }


        return result;
    }






    // 新增可共享用户列表
    @PostMapping("/category/changeToShare")
    public Result changeToShare(@RequestBody Map<String, Object> requestData){
        String nodeid = (String) requestData.get("nodeid");
        String uid_list = (String) requestData.get("uid_list");
        CategoryEntity entity = new CategoryEntity();
        entity.setUidList(uid_list);
        entity.setStatus("1");
        UpdateWrapper<CategoryEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", nodeid);
        int res = categoryMapper.update(entity, updateWrapper);
        if(res == 1){
            return Result.success(200,"修改成功");
        }
        else {
            return Result.fail(500,"修改失败");
        }
    }

    //新增可共享用户列表
    @PostMapping("/category/changeToPrivate")
    public Result changeToPrivate(@RequestBody Map<String, Object> requestData){
        String nodeid = (String) requestData.get("nodeid");
        CategoryEntity entity = new CategoryEntity();
        entity.setUidList("");
        entity.setStatus("0");
        UpdateWrapper<CategoryEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", nodeid);
        int res = categoryMapper.update(entity, updateWrapper);
        if(res == 1){
            return Result.success(200,"修改成功");
        }
        else {
            return Result.fail(500,"修改失败");
        }
    }

    //新增可共享用户列表
    @PostMapping("/category/getNodeInfo")
    public Result getNodeInfo(@RequestBody Map<String, Object> requestData){
        String nodeid = (String) requestData.get("nodeid");
        String uid = (String) requestData.get("uid");

        QueryWrapper<CategoryEntity> queryWrapper = Wrappers.query();
        queryWrapper.eq("id",nodeid);
        CategoryEntity categoryEntity = categoryMapper.selectOne(queryWrapper);
        String includedUids = categoryEntity.getUidList();
        //使用 split() 方法返回的数组是一个固定长度的数组，无法修改其大小。
        //可以使用 Arrays.asList() 方法将数组转换为 ArrayList，然后再添加额外的元素。
        List<String> includedUidList = new ArrayList<>(Arrays.asList(includedUids.split(",")));

        includedUidList.add(uid);

        QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<>();
        userQueryWrapper1.notIn("uid", includedUidList);
        List<User> excludeUserList = userMapper.selectList(userQueryWrapper1);

        QueryWrapper<User> userQueryWrapper2 = new QueryWrapper<>();
        includedUidList.remove(uid);
        userQueryWrapper2.in("uid", includedUidList);
        List<User> includeUserList = userMapper.selectList(userQueryWrapper2);


        //
        List<String> tempRes = new ArrayList<>();
        List<Map<String, Object>> included = new ArrayList<>();
        for (User user : includeUserList) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("key", user.getUid());
            resultMap.put("label", user.getUsername());
            tempRes.add(user.getUid());
            included.add(resultMap);
        }


        List<Map<String, Object>> excluded = new ArrayList<>();
        for (User user : excludeUserList) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("key", user.getUid());
            resultMap.put("label", user.getUsername());
            excluded.add(resultMap);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("included", included);
        result.put("excluded", excluded);
        return  Result.success(200,"操作成功", tempRes);
    }



}
