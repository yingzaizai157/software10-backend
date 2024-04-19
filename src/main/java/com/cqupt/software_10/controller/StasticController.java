package com.cqupt.software_10.controller;

import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.entity.AlgorithmUsage;
import com.cqupt.software_10.entity.AlgorithmUsageDailyStats;
import com.cqupt.software_10.service.StasticOneService;
import com.cqupt.software_10.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/stastic")
public class StasticController {
    @Autowired
    private StasticOneService stasticOneService;
    @Autowired
    private TaskService taskService;
    @GetMapping("/getStasticOne")
    public Result getStasticOne(){
        return Result.success(
                200,"获取统计信息成功",stasticOneService.getStasticOne());
    }
    @GetMapping("/7DaysAlgorithmUsage")
    public List<AlgorithmUsageDailyStats> getAlgorithmUsageDailyStatsLast7Days(){
        //用于获得查询出的数据
        List<AlgorithmUsageDailyStats> resList = taskService.getAlgorithmUsageDailyStatsLast7Days();
        //用于统计所有算法名称
        List<String> AlgorithNames = taskService.getAlgorithmName();
        //补齐res
        boolean nameFlag;
        for (int i=0;i<resList.size();i++)
        {
            List<AlgorithmUsage> usages = resList.get(i).getUsages();
            for (String alName: AlgorithNames){
                nameFlag=false;
                for (int j=0;j<usages.size();j++){
                    if(alName.equals(usages.get(j).getModel())){
                        nameFlag=true;
                        break;
                    }
                }
                if (nameFlag==false){
                    AlgorithmUsage usage = new AlgorithmUsage(alName, 0);
                    usages.add(usage);
                }
            }
        }
        //用于统计七天内的日期
        List<String> dateRange = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //将统计后的日期转换为MM-dd形式
        List<String> processList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        //依次减一天
        for (int i = 0; i < 7; i++) {
            dateRange.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        //处理日期
        for(String proceTime : dateRange)
        {
            processList.add(proceTime.substring(5));
        }
        /**
         * 两种情况
         * 1.七天内没有日期：
         *      设置新对象，add到列表中
         * 2.七天内有日期
         *       找到当天，查看有哪些算法，并依次为算法补充默认值
         */
        //存储所有算法默认信息
        List<AlgorithmUsage> algorithmUsageList = new ArrayList<>();
        for (String name:AlgorithNames){
            algorithmUsageList.add(new AlgorithmUsage(name,0));
        }
        //存储所有结果信息默认值
        List<AlgorithmUsageDailyStats> algorithmUsageDailyStats = new ArrayList<>();
        for (String procTime:processList){
            algorithmUsageDailyStats.add(new AlgorithmUsageDailyStats(procTime,algorithmUsageList,0));
        }
        List<AlgorithmUsageDailyStats> finalResList = new ArrayList<>();
        boolean flag;
        //覆盖
        for (int i = 0; i<algorithmUsageDailyStats.size();i++){
            flag=false;
            for (int j = 0; j<resList.size();j++){
                if(algorithmUsageDailyStats.get(i).getFormattedDate().equals(resList.get(j).getFormattedDate())){
                    flag=true;
                    //如果有日期，替换
                    algorithmUsageDailyStats.set(i, resList.get(j));
                    finalResList.add(algorithmUsageDailyStats.get(i));
                    break;
                }
            }
            if(flag==false){
                finalResList.add(algorithmUsageDailyStats.get(i));
            }
        }
        Collections.reverse(finalResList);
        return finalResList;
    }
    @GetMapping("/getdiseaserate")
    public Result getDiseaseRate(){
        return Result.success(200,"success",stasticOneService.getDiseases());
    }

    @GetMapping("/get_all_table_names")
    public Result getAllTableNames(){
        //获取疾病所有表名
        return Result.success(200,"success",stasticOneService.getAllTableNames());
    }
}
