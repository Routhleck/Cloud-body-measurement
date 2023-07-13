package com.sepbf.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sepbf.backend.mapper.TrainMapper;
import com.sepbf.backend.pojo.Train;
import com.sepbf.backend.pojo.domain.TrainAction;
import com.sepbf.backend.service.TrainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("trainService")
public class TrainServiceImpl extends ServiceImpl<TrainMapper, Train> implements TrainService {

    @Resource
    private TrainMapper trainMapper;

    @Override
    public Train getTrainById(Integer id) {
        return trainMapper.selectById(id);
    }

    @Override
    public boolean addTrain(Train train) {
        return trainMapper.insert(train) > 0;
    }

    @Override
    public List<TrainAction> getTrainByUserIdjoinAction(Integer userId) {
        return trainMapper.selectByUserIdjoinAction(userId);
    }

    @Override
    public Integer getTrainCountByUserId(Integer userId) {
        return trainMapper.getCountByUserId(userId);
    }

    @Override
    public Integer getTotalTime(List<TrainAction> trainActionList) {
        Integer totalTime = 0;
        for (TrainAction trainAction : trainActionList) {
            totalTime += trainAction.getPracticeTime();
        }
        return totalTime;
    }
}
