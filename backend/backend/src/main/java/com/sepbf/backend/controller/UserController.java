package com.sepbf.backend.controller;

import com.sepbf.backend.pojo.User;
import com.sepbf.backend.service.UserService;
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
    public String login(@RequestBody Map<String, Object> map) {
        String name = String.valueOf(map.get("name"));
        String password = (String) map.get("password");

        User user = userService.getUserByName(name);

        //如果用户存在且密码正确
        if (user != null && user.getPassword().equals(password)) {
            // 返回用户ID
            return String.valueOf(user.getUser_id());

        } else {
            return "false";
        }
    }


    @PostMapping("/register")
    public boolean register(@RequestBody Map<String, Object> map) {
        String name = String.valueOf(map.get("name"));
        String password = (String) map.get("password");

        String name_str = String.valueOf(name); // 转换为字符串

        User user = userService.getUserByName(name_str);
        if (user != null) {
            return false; // 用户已存在
        } else {
            // 自动生成独一无二的ID
            int uniqueId = generateUniqueId();
            if (uniqueId < 0) {
                uniqueId = -uniqueId;
            }
            User newUser = new User(uniqueId, name_str, password);
            userService.addUser(newUser);
            return true; // 注册成功
        }
    }


    // 生成唯一ID
    private int generateUniqueId() {
        UUID uuid = UUID.randomUUID();
        return uuid.hashCode();
    }

}
