package com.cqupt.software_10.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.software_10.common.R;
import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.dao.FilterDataColMapper;
import com.cqupt.software_10.dao.FilterDataInfoMapper;
import com.cqupt.software_10.entity.CategoryEntity;
import com.cqupt.software_10.entity.FilterDataCol;
import com.cqupt.software_10.entity.FilterDataInfo;
import com.cqupt.software_10.entity.user.User;
import com.cqupt.software_10.service.CategoryService;
import com.cqupt.software_10.service.LogService;
import com.cqupt.software_10.service.TableDataService;
import com.cqupt.software_10.service.TableDescribeService;
import com.cqupt.software_10.service.user.UserService;
import com.cqupt.software_10.util.SecurityUtil;
import com.cqupt.software_10.vo.FilterConditionVo;
import com.cqupt.software_10.vo.FilterTableDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// TODO 公共模块新增类

@RestController()
@RequestMapping("/api")
public class TableDataController {

    @Autowired
    TableDataService tableDataService;
    @Autowired
    UserService userService;

    @Autowired
    LogService logService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    TableDescribeService tableDescribeService;

    @Autowired
    FilterDataInfoMapper filterDataInfoMapper;

    @Autowired
    FilterDataColMapper filterDataColMapper;


    @GetMapping("/getTableData")
    public Result getTableData(@RequestParam("tableId") String tableId, @RequestParam("tableName") String tableName){
        System.out.println("tableId=="+tableId+"   tableName=="+tableName);
        List<LinkedHashMap<String, Object>> tableData = tableDataService.getTableData(tableId, tableName);
        return Result.success("100",tableData);
    }

    // 文件上传
    // 文件上传
    @PostMapping("/dataTable/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("newName") String tableName,
                             @RequestParam("disease") String type,
                             @RequestParam("user") String user,
                             @RequestParam("uid") String userId,
                             @RequestParam("parentId") String parentId,
                             @RequestParam("parentType") String parentType,
                             @RequestParam("status") String status,
                             @RequestParam("size") Double size,
                             @RequestParam("is_upload") String is_upload,
                             @RequestParam("is_filter") String is_filter){
        // 保存表数据信息
        try {
            List<String> featureList = tableDataService.ParseFileCol(file,tableName);
            tableDataService.uploadFile(file, tableName, type, user, userId, parentId, parentType,status,size,is_upload,is_filter);
            return Result.success("200",featureList); // 返回表头信息
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(500,"文件上传异常");
        }
    }

    // 检查上传文件是数据文件的表名在数据库中是否重复
    @GetMapping("/DataTable/inspection")
    public Result tableInspection(@RequestParam("newname") String name){
        List<CategoryEntity> list = categoryService.list(new QueryWrapper<CategoryEntity>(null));
        boolean flag = true;
        for (CategoryEntity categoryEntity : list) {
            if(categoryEntity.getLabel().equals(name)) {
                flag = false;
                break;
            }
        }
        return Result.success("200",flag); // 判断文件名是否重复
    }


    // 筛选数据后，创建表保存筛选后的数据
    @PostMapping("/createTable")
    public Result createTable(@RequestBody FilterTableDataVo filterTableDataVo, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);

        tableDataService.createTable(filterTableDataVo.getAddDataForm().getDataName(),filterTableDataVo.getAddDataForm().getCharacterList(),
                filterTableDataVo.getAddDataForm().getCreateUser(),filterTableDataVo.getNodeData(),filterTableDataVo.getAddDataForm().getUid(),filterTableDataVo.getAddDataForm().getUsername(),filterTableDataVo.getAddDataForm().getIsFilter(),filterTableDataVo.getAddDataForm().getIsUpload());
        System.out.println("开始新建表："+JSON.toJSONString(filterTableDataVo));

        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，创建数据：" + filterTableDataVo.getAddDataForm().getDataName());
        return Result.success(200,"SUCCESS");
    }
    @PostMapping("/createFilterBtnTable")
    public Result createFilterBtnTable(@RequestBody FilterTableDataVo filterTableDataVo,
                                       @RequestHeader("uid") String userId,
                                       @RequestHeader("username") String username,
                                       @RequestHeader("role") Integer role){
        tableDataService.createFilterBtnTable(filterTableDataVo.getAddDataForm().getDataName(),filterTableDataVo.getAddDataForm().getCharacterList(),
                filterTableDataVo.getAddDataForm().getCreateUser(),filterTableDataVo.getStatus(),filterTableDataVo.getAddDataForm().getUid(),filterTableDataVo.getAddDataForm().getUsername(),filterTableDataVo.getAddDataForm().getIsFilter(),filterTableDataVo.getAddDataForm().getIsUpload(),filterTableDataVo.getAddDataForm().getUid_list(),filterTableDataVo.getNodeid());
//        UserLog userLog = new UserLog();
        // userLog.setId(1);
//        userLog.setUsername(username);
//        userLog.setUid(userId);
//        userLog.setRole(role);
//        userLog.setTime(new Date());
//        userLog.setOperation("用户建表"+filterTableDataVo.getAddDataForm().getDataName());
//        userLogMapper.insert(userLog);
        return Result.success(200,"SUCCESS");
    }

    @PostMapping("/getFilterDataByConditionsByDieaseId")
    public Result<List<Map<String,Object>>> getFilterDataByConditionsByDieaseId(@RequestBody FilterTableDataVo filterTableDataVo){
        List<LinkedHashMap<String, Object>> filterDataByConditions = tableDataService.getFilterDataByConditionsByDieaseId(filterTableDataVo.getAddDataForm().getCharacterList(), filterTableDataVo.getAddDataForm().getUid(),filterTableDataVo.getAddDataForm().getUsername(),filterTableDataVo.getNodeid());
        System.out.println("筛选数据长度为："+filterDataByConditions.size());
        return Result.success("200",filterDataByConditions);
    }


    // 根据条件筛选数据
    @PostMapping("/filterTableData")
    public Result<List<Map<String,Object>>> getFilterTableData(@RequestBody FilterTableDataVo filterTableDataVo){
//        System.out.println("筛选数据长度为："+ filterTableDataVo);

        List<LinkedHashMap<String, Object>> filterDataByConditions = tableDataService.getFilterDataByConditions(filterTableDataVo.getAddDataForm().getCharacterList(), filterTableDataVo.getNodeData());

//        System.out.println("筛选数据长度为："+filterDataByConditions.size());
        return Result.success("200",filterDataByConditions);
    }


    @GetMapping("/getFilterConditionInfos")
    public Result getFilterInfo(){
        ArrayList<FilterConditionVo> vos = new ArrayList<>();
        List<FilterDataInfo> filterDataInfos = filterDataInfoMapper.selectList(null);
        for (FilterDataInfo filterDataInfo : filterDataInfos) {
            FilterConditionVo filterConditionVo = new FilterConditionVo();
            filterConditionVo.setFilterDataInfo(filterDataInfo);
            List<FilterDataCol> filterDataCols = filterDataColMapper.selectList(new QueryWrapper<FilterDataCol>().eq("filter_data_info_id", filterDataInfo.getId()));
            filterConditionVo.setFilterDataCols(filterDataCols);
            vos.add(filterConditionVo);
        }
        return Result.success("200",vos);
    }


    @GetMapping("/getInfoByTableName/{tableName}")
    public Result<List<Map<String,Object>>> getInfoByTableName(@PathVariable("tableName") String tableName){
        tableName = tableName.replace("\"", "");
        List<Map<String, Object>> res = tableDataService.getInfoByTableName(tableName);
        return Result.success(200, "成功", res);
    }

}
