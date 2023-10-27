package com.cqupt.software_10.controller.model;


import com.cqupt.software_10.common.R;
import com.cqupt.software_10.entity.data.ManageData;
import com.cqupt.software_10.entity.model.ManageModel;
import com.cqupt.software_10.service.data.ManageDataService;
import com.cqupt.software_10.service.model.ManageModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ten/models")
public class ManageModelController {
    @Autowired
    private ManageModelService manageModelService;

    @Value("${gorit.file.root.path2}")
    private String filePath;

    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
    private Logger log = LoggerFactory.getLogger("FileController");


    @GetMapping("/all")
    public R<List<ManageModel>> getAllKaggleDiabetesData(){
        List<ManageModel> models = manageModelService.getAllModels();
        return new R<>(200,"成功",models, models.size());
    }


    @GetMapping("/disease")
    public R<List<String>> getDataSetsDisease(){
        List<String> diseases = manageModelService.getModelsDisease();
        return new R<>(200,"成功",diseases, diseases.size());
    }

    @GetMapping("/delete/{id}")
    public void deleteDataSets(@PathVariable("id") int id){
        manageModelService.deleteModelById(id);
    }

    @GetMapping("/search")
    public R<List<ManageModel>> getSearchDataSets(@RequestParam("disease_name") String diseaseName){
        List<ManageModel> models = manageModelService.searchByDiseaseName(diseaseName);
        return new R<>(200,"成功",models, models.size());
    }

    @GetMapping("/update")
    public R<List<ManageModel>> updateSearchById(
            @RequestParam("id") int id,
            @RequestParam("modelName") String modelName,
            @RequestParam("disease") String disease,
            @RequestParam("tableName") String tableName,
            @RequestParam("creator") String creator,
            @RequestParam("remark") String remark
    ){
        System.out.println(id + " " +  tableName + " " +  disease + " " +  creator + " " +  remark);
        ManageModel manageModel = new ManageModel(id, modelName, disease, tableName, creator, remark);
        manageModelService.updateModelInfo(manageModel);
        List<ManageModel> dataSet = manageModelService.getAllModels();

        return new R<>(200,"成功",dataSet, dataSet.size());
    }

    @GetMapping("/saveUpload")
    public R<List<ManageModel>> uploadModel(
//            @RequestParam("id") int id,
            @RequestParam("modelName") String modelName,
            @RequestParam("disease") String disease,
            @RequestParam("tableName") String tableName,
            @RequestParam("creator") String creator,
            @RequestParam("remark") String remark
    ){
        System.out.println("model /upload" +  tableName + " " +  disease + " " +  creator + " " +  remark);
        ManageModel manageModel = new ManageModel(modelName, disease, tableName, creator, remark);
        manageModelService.uploadModel(manageModel);
        List<ManageModel> dataSet = manageModelService.getAllModels();

        return new R<>(200,"成功",dataSet, dataSet.size());
    }

    @PostMapping("/upload2")
    public R<List<ManageModel>> upload2(
                                       MultipartHttpServletRequest request
                                      ) throws Exception {
        List<MultipartFile> files = request.getFiles("file");
        List<ManageModel> dataSet = manageModelService.getAllModels();

        for (int i = 0; i < files.size(); i++) {
            // 得到格式化后的日期
            String format = sdf.format(new Date());
            MultipartFile file = files.get(i);
            // 获取上传的文件名称
            String fileName = file.getOriginalFilename();
            // 时间 和 日期拼接
            String newFileName = format + "_" + fileName + "_" + i;
            // 得到文件保存的位置以及新文件名
            File dest = new File(filePath + newFileName);
            System.out.println("model upload地址：" + filePath + newFileName);

            if (!file.isEmpty()){
                try {
                    // 上传的文件被保存了
                    file.transferTo(dest);
                    // 打印日志
                    log.info("上传成功，当前上传的文件保存在 {}",filePath + newFileName);
                    // 自定义返回的统一的 JSON 格式的数据，可以直接返回这个字符串也是可以的。
                    // return new R<>(200,"上传成功",dataSet, dataSet.size());
                } catch (IOException e) {
                    log.error(e.toString());
                }
                log.info("上传失败，文件 {}",filePath + newFileName);

                System.out.println("第 "+i+" 个文件上传成功");
            }else {
                System.out.println("第 "+i+" 个文件上传失败");
            }
        }
        return new R<>(4044,"上传失败",dataSet, dataSet.size());

    }

    @PostMapping("/upload")
    public R<List<ManageModel>> upload(
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        List<ManageModel> dataSet = manageModelService.getAllModels();

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
