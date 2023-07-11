package com.sepbf.backend.controller;

import com.sepbf.backend.pojo.Test;
import com.sepbf.backend.service.TestService;
import com.sepbf.backend.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private TestService testService;

    @GetMapping
    public Result getTestById(@RequestParam("UserId") Integer userId) {
        Test test = testService.getTestById(userId);
        if (test != null) {
            return Result.success(test);
        } else {
            return Result.error("未找到测试数据");
        }
    }
}
