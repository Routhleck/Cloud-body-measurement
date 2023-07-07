package com.sepbf.backend.controller;

import com.sepbf.backend.pojo.User;
import com.sepbf.backend.service.UserService;
import com.sepbf.backend.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, Object> map) {
        String name = String.valueOf(map.get("name"));
        String password = (String) map.get("password");
        System.out.println(name + password);

        User user = userService.getUserByName(name);

        // 如果用户存在且密码正确
        if (user != null && user.getPassword().equals(password)) {
            return Result.success(user.getUser_id());
        } else {
            return Result.error("用户名或密码错误");
        }
    }



    @PostMapping("/register")
    public Result register(@RequestBody Map<String, Object> map) {
        String name = String.valueOf(map.get("name"));
        String password = (String) map.get("password");

        String name_str = String.valueOf(name); // 转换为字符串

        User user = userService.getUserByName(name_str);
        if (user != null) {
            return Result.error("用户已存在"); // 用户已存在
        } else {
            // 自动生成独一无二的ID
            int uniqueId = generateUniqueId();
            if (uniqueId < 0) {
                uniqueId = -uniqueId;
            }
            User newUser = new User(uniqueId, name_str, password);
            userService.addUser(newUser);
            return Result.success(); // 注册成功
        }
    }


    // 生成唯一ID
    private int generateUniqueId() {
        UUID uuid = UUID.randomUUID();
        return uuid.hashCode();
    }

}
