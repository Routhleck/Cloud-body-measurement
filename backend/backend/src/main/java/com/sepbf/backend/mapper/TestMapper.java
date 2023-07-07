package com.sepbf.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sepbf.backend.pojo.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestMapper extends BaseMapper<Test>{
    int delete(Test test);

    @Select("SELECT * FROM test WHERE user_id = #{user_id}")
    Test selectById(Integer user_id);
}
