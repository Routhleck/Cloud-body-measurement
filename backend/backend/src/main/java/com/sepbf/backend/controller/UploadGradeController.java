package com.sepbf.backend.controller;

import com.sepbf.backend.pojo.Test;
import com.sepbf.backend.service.TestService;
import com.sepbf.backend.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/uploadGrade")
public class UploadGradeController {

    @Resource
    private TestService testService;

    //前端传入id，体测项目，对应的运动个数，后端存入数据库，返回成功与否
    @PostMapping
    public Result uploadGrade(@RequestBody Map<String, Object> map) {

        //把拿到的数据转换为int类型
        int id = Integer.parseInt(String.valueOf(map.get("id")));
        String item = String.valueOf(map.get("item"));
        String number = String.valueOf(map.get("number"));

        //获取当前年份
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        System.out.println("当前年份为："+year);

        //查询该用户该年是否已经有体测数据
        int count = testService.getTestCountByIdAndYear(String.valueOf(id),year);
        System.out.println("查询到的数据条数为："+count);

        //如果没有，插入新数据，其中其他项目的数据为0
        if(count==0){
            //构造新的Test对象，填入数据
            Test test = new Test(0,0,0,0,0,0,0,0,0,0,0,0,0,"");
            test.setUser_id(id);
            test.setTest_time(String.valueOf(year));
            //根据项目名称，填入对应的数据[pullUp, pushUp, sitUp, squat]
            insertGrade(item, number, test);
            //插入新数据
            testService.addTest(test);

            return Result.success();
        }else {
            //如果有，更新对应项目的数据
            //TODO:替换getTestById方法为getTestByIdAndYear方法
            Test test = testService.getTestById(id);
            insertGrade(item, number, test);
            //更新数据
            testService.updateTest(test);
            return Result.success();
        }

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
