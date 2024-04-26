package com.cqupt.software_10.controller;



import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.dao.CategoryMapper;
import com.cqupt.software_10.entity.AdminDataManage;
import com.cqupt.software_10.entity.CategoryEntity;
import com.cqupt.software_10.service.*;
import com.cqupt.software_10.service.user.UserService;
import com.cqupt.software_10.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO 因为操作该模块的用户肯定是管理员，因此在插入日志时将role角色固定为0， 管理员状态

@RestController
@RequestMapping("/api/sysManage")
public class AdminDataManageController {
    @Autowired
    AdminDataManageService adminDataManageService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    UserService userService;
    @Autowired
    private LogService logService;
    // 文件上传
    @PostMapping("/uploadDataTable")
    public Result uploadDataTable(@RequestParam("file") MultipartFile file,
//                             @RequestParam("tableId") String tableId,
                             @RequestParam("pid") String pid,
                             @RequestParam("tableName") String tableName,
                             @RequestParam("userName") String userName,
                             @RequestParam("ids") String[] ids,
                             @RequestParam("uid") String uid,   // 传表中涉及到的用户的uid
                             @RequestParam("tableStatus") String tableStatus,
                             @RequestParam("tableSize") float tableSize,
                             @RequestParam("current_uid") String current_uid //操作用户的uid
        ){
        // 保存表数据信息
        try {
//            String tableId="";
            // 管理员端-数据管理新更改
//            传入的是category的id集合，根据id获取labels拼接成classpath
            String classPath = "公共数据集";
            for (String id : ids){
                CategoryEntity categoryEntity = categoryMapper.selectById(id);
                classPath += "/" + categoryEntity.getLabel();
            }
            classPath += "/" + tableName;
            List<String> featureList = adminDataManageService.uploadDataTable(file, pid, tableName, userName, classPath, uid, tableStatus, tableSize, current_uid);
            return Result.success("200",featureList); // 返回表头信息
        }catch (Exception e){
            e.printStackTrace();
            logService.insertLog(current_uid, 0, e.getMessage());
            return Result.success(500,"文件上传异常");
        }
    }

    // 管理员端-数据管理新更改
    @GetMapping("/selectDataDiseases")
    public Result<AdminDataManage> selectDataDiseases(
            HttpServletResponse response, HttpServletRequest request
//            @RequestParam("current_uid") String current_uid
    ){ // 参数表的Id

        List<CategoryEntity> res = categoryService.getLevel2Label();

        List<Object> retList = new ArrayList<>();
        for (CategoryEntity category : res) {
            Map<String, Object> ret =  new HashMap<>();
            ret.put("label", category.getLabel());
            ret.put("value", category.getId());
            if (selectCategoryDataDiseases(category.getId()).size() > 0) {
                ret.put("children", selectCategoryDataDiseases(category.getId()));
            }

            retList.add(ret);
        }
        System.out.println(retList);


        return Result.success("200",retList);
//        return Result.success("200",adminDataManages);
    }
    // 管理员端-数据管理新更改
    public List<Map<String, Object>> selectCategoryDataDiseases(String pid){
        List<Map<String, Object>> retList = new ArrayList<>();
        List<CategoryEntity> res = categoryService.getLabelsByPid(pid);
        for (CategoryEntity category : res) {
            Map<String, Object> ret =  new HashMap<>();
            ret.put("label", category.getLabel());
            ret.put("value", category.getId());
            if (selectCategoryDataDiseases(category.getId()).size() > 0) {
                ret.put("children", selectCategoryDataDiseases(category.getId()));
            }
            retList.add(ret);
        }
        return retList;
    }

    @GetMapping("/selectAdminDataManage")
    public Result<AdminDataManage> selectAdminDataManage(
            HttpServletRequest request
//            @RequestParam("current_uid") String current_uid
    ){ // 参数表的Id
        String token = request.getHeader("Authorization");
        // 读取名为 "Authorization" 的请求头中的值（这里假设是 Token）
        System.out.println("Token from header: " + token);
        String uid = SecurityUtil.getUserIdFromToken(token);
        System.out.println("uid from token" + uid);
        List<AdminDataManage> adminDataManages = adminDataManageService.selectAllDataInfo();
//        System.out.println("数据为："+ JSON.toJSONString(tableDescribeEntity));
        Map<String, Object> ret =  new HashMap<>();
        ret.put("list", adminDataManages);
        ret.put("total", adminDataManages.size());

        return Result.success("200",ret);
//        return Result.success("200",adminDataManages);
    }

    @GetMapping("/selectDataByName")
    public Result<AdminDataManage> selectDataByName(
            @RequestParam("searchType") String searchType,
            @RequestParam("name") String name
//            @RequestParam("current_uid") String current_uid
    ){
        List<AdminDataManage> adminDataManages = adminDataManageService.selectDataByName(searchType, name);
//        System.out.println("数据为："+ JSON.toJSONString(tableDescribeEntity));
        Map<String, Object> ret =  new HashMap<>();
        ret.put("list", adminDataManages);
        ret.put("total", adminDataManages.size());

        return Result.success("200",ret);
    }

    @GetMapping("/selectDataById")
    public Result<AdminDataManage> selectDataById(
            @RequestParam("id") String id
//            @RequestParam("current_uid") String current_uid
    ){
        AdminDataManage adminDataManage = adminDataManageService.selectDataById(id);
        CategoryEntity categoryEntity = categoryMapper.selectById(id);
        CategoryEntity parentCategoryEntity = categoryMapper.selectById(categoryEntity.getParentId());
        Map<String, Object> ret =  new HashMap<>();
        ret.put("object", adminDataManage);
        ret.put("id", parentCategoryEntity.getId());

        return Result.success("200",ret);
    }

    @GetMapping("/updateAdminDataManage")
    public Result<AdminDataManage> updateAdminDataManage(
            @RequestParam("id") String id,
            @RequestParam("tableid") String tableid,
            @RequestParam("oldTableName") String oldTableName,
            @RequestParam("tableName") String tableName,
            @RequestParam("tableStatus") String tableStatus,
            @RequestParam("current_uid") String current_uid
            ){
        adminDataManageService.updateInfo(id, tableid, oldTableName, tableName, tableStatus, current_uid);

        return Result.success("200","已经更改到数据库");
    }

    @GetMapping("/getLevel2Label")
    public Result<List<CategoryEntity>> getLevel2Label(
//            @RequestParam("current_uid") String current_uid
    ){
        List<CategoryEntity> res = categoryService.getLevel2Label();
        return Result.success("200",res);
    }
    @GetMapping("/getLabelByPid")
    public Result<List<CategoryEntity>> getLabelsByPid(
            @RequestParam("pid") String pid
//            @RequestParam("current_uid") String current_uid
    ){
        List<CategoryEntity> res = categoryService.getLabelsByPid(pid);
        return Result.success("200",res);
    }

    @GetMapping("/deleteByTableName")
    public Result<AdminDataManage> deleteByTableName(
            @RequestParam("id") String id,
            @RequestParam("uid") String uid,
            @RequestParam("tableSize") float tableSize,
            @RequestParam("tableId") String tableId,
            @RequestParam("tableName") String tableName,
            @RequestParam("current_uid") String current_uid
    ){
//        System.out.println();

        adminDataManageService.deleteByTableName(tableName);// 【因为数据库中表名是不能重名的】
        logService.insertLog(current_uid, 0, "删除了public模式下储存的表:" + tableName);
        adminDataManageService.deleteByTableId(tableId);// 在table_describe中删除记录
        logService.insertLog(current_uid, 0, "删除了table_describe表中的"+tableName+"记录信息");
        categoryService.removeNode(tableId);// 在category中设置is_delete为1
        logService.insertLog(current_uid, 0, "在category中设置is_delete为1" );

//        float tableSize = adminDataManage.getTableSize();
        userService.addTableSize(uid, tableSize);
        logService.insertLog(current_uid, 0, "在user表中修改容量" );
        return Result.success("200","已在数据库中删除了"+tableName+"表");
    }
}
