package com.sepbf.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sepbf.backend.pojo.Train;
import com.sepbf.backend.pojo.domain.TrainAction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TrainMapper extends BaseMapper<Train>  {
    @Select("SELECT * FROM practice WHERE user_id = #{user_id}")
    Train selectById(Integer user_id);

    List<Train> selectByUserId(Integer userId);

    @Select("SELECT t.count, t.value, t.time, t.practice_time, a.name " +
            "FROM train AS t " +
            "INNER JOIN action AS a ON t.action_id = a.action_id " +
            "WHERE t.user_id = #{userId}")
    List<TrainAction> selectByUserIdjoinAction(Integer userId);

    @Select("SELECT COUNT(*) FROM train WHERE user_id = #{userId}")
    Integer getCountByUserId(Integer userId);
}
