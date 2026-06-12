package com.example.mealplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mealplan.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
    @Select("SELECT * FROM notice ORDER BY create_time DESC LIMIT #{limit}")
    List<Notice> selectRecentNotices(@Param("limit") int limit);
}