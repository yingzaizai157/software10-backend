package com.cqupt.software_10.controller.data;


import com.cqupt.software_10.common.R;
import com.cqupt.software_10.entity.TenKaggleDiabetes;
import com.cqupt.software_10.entity.data.ManageData;
import com.cqupt.software_10.entity.model.ManageModel;
import com.cqupt.software_10.service.data.KaggleDiabetesService;
import com.cqupt.software_10.service.data.ManageDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ten/datasets")
public class ManageDataController {
    @Autowired
    private ManageDataService manageDataService;

    @Value("${gorit.file.root.path1}")
    private String filePath;

    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
    private Logger log = LoggerFactory.getLogger("FileController");

    @GetMapping("/all")
    public R<List<ManageData>> getDataSets(){
        List<ManageData> dataSet = manageDataService.getAllDataSet();
        return new R<>(200,"成功",dataSet, dataSet.size());
    }

    @GetMapping("/disease")
    public R<List<String>> getDataSetsDisease(){
        List<String> diseases = manageDataService.getDataSetDisease();
        return new R<>(200,"成功",diseases, diseases.size());
    }

    @GetMapping("/delete/{id}")
    public void deleteDataSets(@PathVariable("id") int id){
        manageDataService.deleteDataSetsById(id);
    }

    @GetMapping("/search")
    public R<List<ManageData>> getSearchDataSets(@RequestParam("disease_name") String diseaseName){
        List<ManageData> dataSet = manageDataService.searchByDiseaseName(diseaseName);
        return new R<>(200,"成功",dataSet, dataSet.size());
    }

    @GetMapping("/update")
    public R<List<ManageData>> updateSearchById(
            @RequestParam("id") int id,
            @RequestParam("tableName") String tableName,
            @RequestParam("disease") String disease,
            @RequestParam("creator") String creator,
            @RequestParam("remark") String remark
    ){
        System.out.println(id + " " +  tableName + " " +  disease + " " +  creator + " " +  remark);
        ManageData manageData = new ManageData(id, tableName, disease, creator, remark);
        manageDataService.updateDatasetsInfo(manageData);
        List<ManageData> dataSet = manageDataService.getAllDataSet();

        return new R<>(200,"成功",dataSet, dataSet.size());
    }

    @GetMapping("/saveUpload")
    public R<List<ManageData>> uploadData(
            @RequestParam("tableName") String tableName,
            @RequestParam("disease") String disease,
            @RequestParam("creator") String creator,
            @RequestParam("remark") String remark
    ){
        System.out.println("上传: /upload" + tableName + " " +  disease + " " +  creator + " " +  remark);
        ManageData manageData = new ManageData(tableName, disease, creator, remark);
        manageDataService.uploadData(manageData);
        List<ManageData> dataSet = manageDataService.getAllDataSet();

        return new R<>(200,"成功",dataSet, dataSet.size());
    }

    @PostMapping("/upload")
    public R<List<ManageData>> upload(
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        List<ManageData> dataSet = manageDataService.getAllDataSet();

        // 得到格式化后的日期
        String format = sdf.format(new Date());
        // 获取上传的文件名称
        String fileName = file.getOriginalFilename();
        // 时间 和 日期拼接
        String newFileName = format + "_" + fileName;
        // 得到文件保存的位置以及新文件名
        File dest = new File(filePath + newFileName);
        System.out.println("model upload地址：" + filePath + newFileName);

        if (!file.isEmpty()){
            try {
                // 上传的文件被保存了
                file.transferTo(dest);
                // 打印日志
                log.info("上传成功，当前上传的文件保存在 {}",filePath + newFileName);
                return new R<>(200,"上传成功",dataSet, dataSet.size());

            } catch (IOException e) {
                log.error(e.toString());
                log.info("上传失败，文件 {}",filePath + newFileName);
            }
        }else {
            System.out.println(fileName+"文件上传失败");
        }
        return new R<>(4044,"上传失败",dataSet, dataSet.size());

    }
}
