package com.cqupt.software_10.service.data;

import com.cqupt.software_10.entity.data.Person;

import java.util.List;

public interface PersonService {
    List<Person> getPersonData();

    void updatePersonData(Person person);

    Double getPersonDataValue(String field);
}
