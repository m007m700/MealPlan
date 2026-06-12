package com.example.mealplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mealplan.entity.OrderRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderRecordMapper extends BaseMapper<OrderRecord> {
    @Select("SELECT * FROM order_record WHERE meal_plan_id = #{mealPlanId}")
    List<OrderRecord> selectByMealPlanId(@Param("mealPlanId") Long mealPlanId);

    @Select("SELECT dish_id, COUNT(*) as count FROM order_record WHERE meal_plan_id = #{mealPlanId} GROUP BY dish_id")
    List<OrderStatistics> selectStatisticsByMealPlanId(@Param("mealPlanId") Long mealPlanId);

    @Delete("DELETE FROM order_record WHERE meal_plan_id = #{mealPlanId}")
    void deleteByMealPlanId(@Param("mealPlanId") Long mealPlanId);
}