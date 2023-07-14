package com.sepbf.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sepbf.backend.pojo.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService extends IService<User> {

    boolean addUser(User user);

    boolean deleteUser(User user);

    boolean updateUser(User user);

    User getUserById(Integer id);

    User getUserByName(String name);

}
