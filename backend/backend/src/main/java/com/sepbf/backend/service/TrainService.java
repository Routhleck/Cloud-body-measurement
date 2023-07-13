package com.sepbf.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sepbf.backend.pojo.Train;
import org.springframework.stereotype.Component;

@Component
public interface TrainService extends IService<Train>{
    Train getTrainById(Integer id);

    boolean addTrain(Train train);

}
