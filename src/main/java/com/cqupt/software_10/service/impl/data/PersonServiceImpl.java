package com.cqupt.software_10.service.impl.data;

import com.cqupt.software_10.entity.TenKaggleDiabetes;
import com.cqupt.software_10.entity.data.Person;
import com.cqupt.software_10.mapper.data.KaggleDiabetesDataMapper;
import com.cqupt.software_10.mapper.data.PersonMapper;
import com.cqupt.software_10.service.data.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonMapper personMapper;

    @Override
    public List<Person> getPersonData() {
        List<Person> person = personMapper.getPersonData();
        return person;
    }

    @Override
    public void updatePersonData(Person person) {
        personMapper.updatePersonData(person);
    }

    @Override
    public Double getPersonDataValue(String field) {
        return personMapper.getPersonDataValue(field);
    }
}
