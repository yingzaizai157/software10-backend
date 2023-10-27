package com.cqupt.software_10.controller.data;


import com.cqupt.software_10.common.R;
import com.cqupt.software_10.entity.TenKaggleDiabetes;
import com.cqupt.software_10.entity.data.Person;
import com.cqupt.software_10.mapper.data.PersonMapper;
import com.cqupt.software_10.service.data.KaggleDiabetesService;
import com.cqupt.software_10.service.knowledge.FeaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ten/data")
public class PersonController {
    @Autowired
    private PersonMapper personMapper ;
    @Autowired
    private FeaturesService featuresService;

    @GetMapping("/person")
    public R<List<Person>> getPersonData(){
        List<Person> person = personMapper.getPersonData();
        return new R<>(200,"成功",person, 1);
    }

    @GetMapping("/personData")
    public R<List<Double>> getPersonDataValue(){
        List<Double> personData = new ArrayList<>();


        List<String> fields = featuresService.getAllFeaturesENName();
        System.out.println("================= personData: features ============");
        System.out.println(fields);
        for (int i = 0; i < fields.size(); i++) {
            Double value = personMapper.getPersonDataValue(fields.get(i));
            personData.add(value);
        }
        System.out.println(personData);
        return new R<>(200,"成功",personData, 1);
    }



    @PostMapping("/update_person2")
    public void update(){
        System.out.println("==================update");
    }



    @PostMapping("/update_person")
    public void updatePersonData(
//                                 @RequestParam("age") int age,
//                                 @RequestParam("pregnancies") int pregnancies,
//                                 @RequestParam("diabetesPedigreeFunction") double diabetesPedigreeFunction,
//                                 @RequestParam("glucose") double glucose,
//                                 @RequestParam("bloodPressure") double bloodPressure,
//                                 @RequestParam("skinThickness") double skinThickness,
//                                 @RequestParam("insulin") double insulin,
//                                 @RequestParam("bmi") double bmi

//            @RequestParam("age") String age,
//            @RequestParam("pregnancies") String pregnancies,
//            @RequestParam("diabetesPedigreeFunction") String diabetesPedigreeFunction,
//            @RequestParam("glucose") String glucose,
//            @RequestParam("bloodPressure") String bloodPressure,
//            @RequestParam("skinThickness") String skinThickness,
//            @RequestParam("insulin") String insulin,
//            @RequestParam("bmi") String bmi

            @RequestBody Map<String, String> params

    ){


//        System.out.println(age + "  " + pregnancies + "  " + diabetesPedigreeFunction + "  " + glucose + "  " +
//                bloodPressure + "  " + skinThickness + "  " + insulin + "  " + bmi);

        Person person = new Person(
                Integer.parseInt(params.get("age")),
                Integer.parseInt(params.get("pregnancies")),
                Double.parseDouble(params.get("diabetesPedigreeFunction")),
                Double.parseDouble(params.get("glucose")),
                Double.parseDouble(params.get("bloodPressure")),
                Double.parseDouble(params.get("skinThickness")),
                Double.parseDouble(params.get("insulin")),
                Double.parseDouble(params.get("bmi"))
                );


//                pregnancies, diabetesPedigreeFunction, glucose, bloodPressure, skinThickness, insulin, bmi);
        personMapper.updatePersonData(person);
        System.out.println("person"+ "更新成功");
    }
}
