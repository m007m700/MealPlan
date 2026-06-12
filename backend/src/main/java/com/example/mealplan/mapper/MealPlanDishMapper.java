package com.example.mealplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mealplan.entity.MealPlanDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MealPlanDishMapper extends BaseMapper<MealPlanDish> {
    @Select("SELECT dish_id FROM meal_plan_dish WHERE meal_plan_id = #{mealPlanId}")
    List<Long> selectDishIdsByMealPlanId(@Param("mealPlanId") Long mealPlanId);

    @Delete("DELETE FROM meal_plan_dish WHERE meal_plan_id = #{mealPlanId}")
    void deleteByMealPlanId(@Param("mealPlanId") Long mealPlanId);
}