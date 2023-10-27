package com.cqupt.software_10.mapper;

import com.cqupt.software_10.entity.TenKaggleDiabetes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TenKaggleDiabetesMapper {

    List<TenKaggleDiabetes> getAllKaggleDiabetes();

}
