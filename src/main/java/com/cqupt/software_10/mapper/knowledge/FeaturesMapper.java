package com.cqupt.software_10.mapper.knowledge;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.software_10.entity.TenDiabetes;
import com.cqupt.software_10.entity.data.ManageData;
import com.cqupt.software_10.entity.knowledge.Features;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeaturesMapper  extends BaseMapper<TenDiabetes> {

    List<String> getKnowledgeDisease();

    List<String> getKnowledgeFactorCN();

    void deleteKnowledgeById(@Param("id") int id);

    List<Features> searchByDiseaseName(@Param("diseaseName") String diseaseName, @Param("risks") String risks);

    void updateKnowledgeInfo(@Param("knowledge") Features knowledge);

    void addKnowledgeInfo(@Param("knowledge") Features knowledge);

    List<Features> getAllFeaturesKnowledges();

    List<String> getAllFeaturesENName();

    String getExceptionKnowledge(@Param("disease") String disease, @Param("field") String field);

    void setDoctorRate(@Param("DoctorRate") Features feature);


}
