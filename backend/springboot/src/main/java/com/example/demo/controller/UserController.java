package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.Result;
import com.example.demo.entity.*;
import com.example.demo.enums.PwdEnum;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; //注入bcryct加密

    @PostMapping("/login")
    public Result<?> login(@RequestBody User userParam) {
        User userPwd = userMapper.selectByName(userParam.getName());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", userParam.getName());
        queryWrapper.eq("password", userPwd.getPassword());
        User res = userMapper.selectOne(queryWrapper);

        // 判断密码是否正确
        if (!bCryptPasswordEncoder.matches(userParam.getPassword(), userPwd.getPassword())) {
            return Result.error("-1", "密码错误");
        }
        if (res == null) {
            return Result.error("-1", "用户名或密码错误");
        }

        return Result.success(res);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getName, user.getName()));
        if (res != null) {
            return Result.error("-1", "用户名重复");
        }
//        if (user.getPassword() == null) {
//            user.setPassword("123456");
//        }
        User userInfo = User.builder()
                .name(user.getName())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .build();

        userMapper.insert(userInfo);
        return Result.success();
    }

    @PostMapping
    public Result<?> save(@RequestBody User user) {
        if (user.getPassword() == null) {
            user.setPassword(bCryptPasswordEncoder.encode(PwdEnum.PASSWORD.getPassword()));
        }
        userMapper.insert(user);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody User user) {
        userMapper.updateById(user);
        return Result.success();
    }

    @PutMapping("/pass")
    public Result<?> pass(@RequestBody Map<String, Object> map) {
        User user = userMapper.selectById((Integer) map.get("userId"));
        if (user== null) {
            return Result.error("-1", "未找到用户");
        }
        if (!bCryptPasswordEncoder.matches(map.get("password").toString(), user.getPassword())) {
            return Result.error("-1", "密码错误");
        }
        map.put("newPass", (bCryptPasswordEncoder.encode(map.get("newPass").toString())));
        userMapper.updatePass(map);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(userMapper.selectById(id));
    }

    @GetMapping("/all")
    public Result<?> findAll() {
        return Result.success(userMapper.selectList(null));
    }



}
