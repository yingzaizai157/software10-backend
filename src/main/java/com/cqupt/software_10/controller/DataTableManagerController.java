package com.cqupt.software_10.controller;


import com.cqupt.software_10.common.DataTable;
import com.cqupt.software_10.common.R;
import com.cqupt.software_10.common.TableManagerDTO;
import com.cqupt.software_10.common.UploadResult;
import com.cqupt.software_10.entity.TableManager;
import com.cqupt.software_10.mapper.DataTableManagerMapper;
import com.cqupt.software_10.service.DataTableManagerService;
import com.cqupt.software_10.service.FileService;
import com.cqupt.software_10.service.TableManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/DataTable")
public class DataTableManagerController {
    String  dbUrl = "jdbc:postgresql://schh.work:2208/software4v2";
    String dbUsername = "postgres";
    String dbPassword = "111111";

    MultipartFile loadFile;

    @Autowired
    private DataTableManagerService dataTableManagerService;

    @Autowired
    private FileService fileService;

    @Resource
    private DataTableManagerMapper dataTableManagerMapper;

    @Autowired
    private TableManagerService tableManagerService;

    public DataTableManagerController(DataTableManagerService dataTableManagerService, FileService fileService, TableManagerService tableManagerService) {
        this.dataTableManagerService = dataTableManagerService;
        this.fileService = fileService;
        this.tableManagerService = tableManagerService;
    }

    /**
     * 获取表管理表所有信息
     *
     * @return
     */
    @GetMapping("/upall")
    public List<DataTable> upall() {
        return dataTableManagerService.upalldata();
    }




    @GetMapping("/inspection")
    public boolean test_name(@RequestParam("name") String name){
        System.out.println("name" + name);
        List<String> usedTableNames = dataTableManagerService.upname();
        if (usedTableNames.contains(name)) {
            return  false;
        }else{
            return true;
        }
    }
    /**
     * 获取表data_table的所有内容
     *
     * @return
     */
    @GetMapping("/getTables")
    public R<List<DataTable>> getTables(){
        List<DataTable> tables = dataTableManagerService.upalldata();
        //
        return new R<>(200,"成功",tables, tables.size());
    }


    // 模型训练表预览
//    @GetMapping("/getData")
//    public R<List<DataTable>> getData(
//            @RequestParam("tableName") String tableName
//    ){
//        // 先获取此表信息
//        DataTable tableInfo = dataTableManagerService.getTableInfo(tableName);
//    }

    /**
     *  从陈鹏那里拷贝来
     */
    @PostMapping("/upload")
    public UploadResult uploadFile(@RequestPart("file") MultipartFile file,
                                   @RequestParam("newName") String newName,
                                   @RequestParam("disease") String disease,
                                   @RequestParam("createName") String createName) {
        try {
            System.out.println("你好");
            loadFile = file;
            return fileService.fileUpload(file, newName, disease, createName);
        } catch (Exception e) {
            UploadResult res = new UploadResult();
            res.setCode(500);
            System.out.println("错误信息: " + e);
            res.setE(e);
            return res;
        }
    }

    @PostMapping("/delete/{tableName}")
    public List<DataTable> delete(@PathVariable("tableName") String tableName ) {
        try {
            System.out.println("删表");
            // 根据表名删除表
            Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            Statement statement = connection.createStatement();
            String drop = "Drop table " +"`"+tableName+"`" ;
            statement.executeUpdate(drop);


            String deleteTableNameFromData_table = "DELETE from data_table where table_name = "  +"\""+tableName+"\"";
            statement.executeUpdate(deleteTableNameFromData_table);

            String deleteTableNameFromT_table_manage = "DELETE from t_table_manager where table_name = "  +"\""+tableName+"\"";
            statement.executeUpdate(deleteTableNameFromT_table_manage);

        } catch (Exception e) {
            System.out.println("删表失败");
        }

        return upall();
    }

    @PostMapping("/manage")
    public R<List<TableManager>> selectAllColumn() {

        List<TableManager> allData = tableManagerService.getAllData();
        return new R<>(200,"成功",allData, allData.size());

    }

    @PostMapping("/insertTableManager")
    public UploadResult insertTableManager(@RequestBody TableManagerDTO tableManagerDTO) {
        try {
            tableManagerService.insertTableManager(tableManagerDTO);

            List<DataTable> d=dataTableManagerService.upalldata();

            UploadResult res =new UploadResult();
            res.setRes(d);
            return res;
        } catch (Exception e) {
            UploadResult res = new UploadResult();
            res.setCode(500);
            res.setE(e);
            dataTableManagerService.deletename(tableManagerDTO.getTableName());
            dataTableManagerMapper.deletetablename(tableManagerDTO.getTableName());
            return res;
        }
    }

    @PostMapping("/uploadTable")
    public UploadResult uploadTable(
                                    @RequestParam("newName") String newName,
                                    @RequestParam("disease") String disease,
                                    @RequestParam("createName") String createName,
                                    @RequestParam("uid") String uid
    ) {
        try {
            int userid = Integer.parseInt(uid);
            UploadResult result = fileService.creatUpTable(loadFile, newName,disease, createName);
            dataTableManagerService.updateDataTable(newName,disease, createName, userid);
            return result;
        } catch (Exception e) {
            System.out.println("create up table");
            UploadResult res =new UploadResult();
            res.setCode(500);
            System.out.println(e);
            res.setE(e);
            return res;
        }
    }


}