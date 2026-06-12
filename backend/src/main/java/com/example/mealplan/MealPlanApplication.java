package com.example.mealplan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.example.mealplan.mapper")
@EnableScheduling
public class MealPlanApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealPlanApplication.class, args);
    }
}