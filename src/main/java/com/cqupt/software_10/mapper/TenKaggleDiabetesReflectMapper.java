package com.cqupt.software_10.mapper;

import com.cqupt.software_10.entity.TenKaggleDiabetesReflect;
import com.cqupt.software_10.entity.TenMedicalKnowledgeBaseReflect;
import com.cqupt.software_10.entity.pojo.TwoStringDouble;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TenKaggleDiabetesReflectMapper {
    List<String> getKaggleDiabetesReflectColumn();

    List<TenKaggleDiabetesReflect> getKaggleDiabetesReflect(@Param("model") String  model);

    String getKaggleDiabetesReflectCnByEn(@Param("enName") String enName);


    void updateContributorByCnCol(@Param("twoStringDouble") TwoStringDouble twoStringDouble);

    void deleteContributorByCnCol(@Param("model") String  model,
                                  @Param("colName") String colName);


}
