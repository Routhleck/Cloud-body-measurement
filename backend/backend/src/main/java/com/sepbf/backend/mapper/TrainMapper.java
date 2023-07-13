package com.sepbf.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sepbf.backend.pojo.Train;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TrainMapper extends BaseMapper<Train>  {
    @Select("SELECT * FROM practice WHERE user_id = #{user_id}")
    Train selectById(Integer user_id);
}
