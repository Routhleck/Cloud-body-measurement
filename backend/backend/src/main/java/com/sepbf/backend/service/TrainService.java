package com.sepbf.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sepbf.backend.pojo.Train;
import com.sepbf.backend.pojo.domain.TrainAction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TrainService extends IService<Train>{
    Train getTrainById(Integer id);

    boolean addTrain(Train train);

    List<TrainAction> getTrainByUserIdjoinAction(Integer userId);

    Integer getTrainCountByUserId(Integer userId);

    Integer getTotalTime(List<TrainAction> trainActionList);
}
