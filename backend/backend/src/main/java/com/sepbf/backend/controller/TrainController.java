package com.sepbf.backend.controller;

import com.sepbf.backend.pojo.domain.TrainAction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sepbf.backend.pojo.Train;
import com.sepbf.backend.service.TrainService;
import com.sepbf.backend.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/train")
public class TrainController {
    @Resource
    private TrainService trainService;

    @GetMapping
    public Result findAllByUserId(@RequestParam("UserId") Integer userId) {
        return Result.success(trainService.getTrainById(userId));
    }

    @GetMapping("/record")
    public Result getTrainByUserIdjoinAction(@RequestParam("UserId") Integer userId) {
        Map<String, Object> map = new HashMap<>();
        List<TrainAction> tempList = trainService.getTrainByUserIdjoinAction(userId);
        map.put("ItemList", trainService.getTrainByUserIdjoinAction(userId));
        map.put("totalTime", trainService.getTotalTime(tempList));
        map.put("totalCount", tempList.size());

        return Result.success(map);
    }
}
