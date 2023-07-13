package com.sepbf.backend.controller;

import com.sepbf.backend.pojo.Action;
import com.sepbf.backend.pojo.Test;
import com.sepbf.backend.pojo.Train;
import com.sepbf.backend.service.ActionService;
import com.sepbf.backend.service.TestService;
import com.sepbf.backend.service.TrainService;
import com.sepbf.backend.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/uploadTrainGrade")
public class TrainGradeController {

    @Resource
    private TrainService trainService;

    @Resource
    private ActionService actionService;

    //前端传入id，体测项目，对应的运动个数，后端存入数据库，返回成功与否
    @PostMapping
    public Result uploadGrade(@RequestBody Map<String, Object> map) {

        //把拿到的数据转换为int类型
        int id = Integer.parseInt(String.valueOf(map.get("id")));
        String item = String.valueOf(map.get("item"));
        int number = Integer.parseInt(String.valueOf(map.get("number")));
        int action_time = Integer.parseInt(String.valueOf(map.get("train_time")));

        //获取当前时间并转化为字符串
//        Calendar cal = Calendar.getInstance();
//        System.out.println("当前时间为："+cal.getTime());
//        String year = String.valueOf(cal.getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String year = formatter.format(LocalDateTime.now());
        System.out.println(year); //2018-Dec-21 11:14:12


        //用查到的item找到对应的practice_id
        int action_id = actionService.getActionIdByActionName(item);

        //插入对应的数据
        Train train = new Train(id,action_id,year,action_time, number,0);
        trainService.addTrain(train);

        return Result.success();






    }

    private static void insertGrade(String item, String number, Test test) {
        switch (item){
            case "pullUp":
                test.setPull_up(Integer.parseInt(number));
                System.out.println("引体向上个数为："+ test.getPull_up());
                break;
            case "pushUp":
                test.setPush_up(Integer.parseInt(number));
                System.out.println("俯卧撑个数为："+ test.getPush_up());
                break;
            case "sitUp":
                test.setSit_up(Integer.parseInt(number));
                System.out.println("仰卧起坐个数为："+ test.getSit_up());
                break;
            case "squat":
                test.setSquat(Integer.parseInt(number));
                System.out.println("深蹲个数为："+ test.getSquat());
                break;
            default:
                System.out.println("项目名称错误");
                break;

        }
    }


}
