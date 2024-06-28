package com.cqupt.software_10.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

// TODO 公共模块新增类

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "field_management", schema = "software10")
public class FieldManagementEntity {
//    @TableId
//    private Integer characterId;
//    private String featureName;
//    private String chName;
//    private Boolean diseaseStandard;
//
//    // 人口学
//    private Boolean diagnosis;
//    // 生理指标
//    private Boolean pathology;
//
//    // 行为学
//    private Boolean vitalSigns;
//    private String tableName;
//    private String unit;
//    private Boolean isLabel;
//    private Boolean discrete;
//    private String range;
//    private String disease;

//    private Integer characterId;
//    private String featureName;
//    private String chName;
//    private Boolean diseaseStandard;
//    private Boolean isDemography;
//    private Boolean isPhysiological;
//    private Boolean isSociology;
//    private String tableName;
//    private String unit;
//    private Boolean isLabel;
//    private Boolean discrete;
//    private String range;
//    private String disease;
//    private Integer she;
//    private String isClinicaRelationship;
//    private String isMultipleDiseases;
//    private String isRoomInformation;
//    private String isQuestionnaire;
//    private String isTimeInformation;
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private Timestamp startTime;
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private Timestamp endTime;
//    private String timeSpace;
//    private Date createTime;
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private Timestamp updateTime;
//    private String tablePeople;
//    private String tableOrigin;
//    private String type;


    @TableId
    private Integer characterId;
    private String featureName;
    private String chName;
    private Boolean diseaseStandard;

    // 人口学
    private Boolean population;
    // 生理指标
    private Boolean physiology;

    // 行为学
    private Boolean society;
    private String tableName;
    private String unit;
    private Boolean isLabel;
    private Boolean discrete;
    private String range;
    private String disease;


}
