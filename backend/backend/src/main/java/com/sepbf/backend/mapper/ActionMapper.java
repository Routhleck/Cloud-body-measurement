package com.sepbf.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sepbf.backend.pojo.Action;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ActionMapper extends BaseMapper<Action>{

    @Select("SELECT limit_time FROM action WHERE name = #{name}")
    Integer selectLimitTimeByName(String name);

}
