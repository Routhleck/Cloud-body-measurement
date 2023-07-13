package com.sepbf.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sepbf.backend.pojo.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TestMapper extends BaseMapper<Test>{
    int delete(Test test);

    @Select("SELECT * FROM test WHERE user_id = #{user_id}")
    Test selectById(Integer user_id);

    @Select("SELECT COUNT(*) FROM test WHERE user_id = #{id} AND test_time = #{year}")
    int getTestCountByIdAndYear(String id, int year);


}
