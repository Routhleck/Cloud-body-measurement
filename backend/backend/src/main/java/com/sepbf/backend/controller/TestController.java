package com.sepbf.backend.controller;

import com.sepbf.backend.pojo.Test;
import com.sepbf.backend.service.TestService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private TestService testService;

    @GetMapping
    public Test getTestById(@RequestParam("UserId") Integer UserId) {
        return testService.getTestById(UserId);
    }
}
