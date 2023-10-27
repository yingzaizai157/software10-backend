package com.cqupt.software_10.mapper.data;

import com.cqupt.software_10.entity.data.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonMapper {
    List<Person> getPersonData();

    void updatePersonData(Person person);

    Double getPersonDataValue(@Param("field") String field);


}
