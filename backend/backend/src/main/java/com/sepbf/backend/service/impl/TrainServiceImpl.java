package com.sepbf.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sepbf.backend.mapper.TrainMapper;
import com.sepbf.backend.pojo.Train;
import com.sepbf.backend.service.TrainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("trainService")
public class TrainServiceImpl extends ServiceImpl<TrainMapper, Train> implements TrainService {

    @Resource
    private TrainMapper trainMapper;

    @Override
    public Train getTrainById(Integer id) {
        return trainMapper.selectById(id);
    }

}
