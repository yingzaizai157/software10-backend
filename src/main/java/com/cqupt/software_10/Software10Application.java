package com.cqupt.software_10;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cqupt.software_10.mapper")
public class Software10Application {

    public static void main(String[] args) {
        SpringApplication.run(Software10Application.class, args);
    }

}
