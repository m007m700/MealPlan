package com.example.mealplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mealplan.common.MealType;
import com.example.mealplan.entity.MealPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MealPlanMapper extends BaseMapper<MealPlan> {
    @Select("SELECT * FROM meal_plan WHERE meal_date = #{date} AND meal_type = #{mealType}")
    MealPlan selectTodayMeal(@Param("date") LocalDate date, @Param("mealType") MealType mealType);

    @Select("SELECT * FROM meal_plan ORDER BY create_time DESC LIMIT #{limit}")
    List<MealPlan> selectRecentMeals(@Param("limit") int limit);
}