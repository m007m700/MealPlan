package com.example.mealplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mealplan.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    @Select("SELECT * FROM dish WHERE status = 1")
    List<Dish> selectAllEnabled();
}