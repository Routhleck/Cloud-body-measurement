package com.sepbf.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user")
@Data
public class User {

    //用户id自增
    @TableId
    private int user_id;

    private String name;

    private String password;

    private Boolean is_admin;

    //构造函数
    public User(int user_id, String name, String password) {
        this.user_id = user_id;
        this.name = name;
        this.password = password;
        this.is_admin = false;
    }

}
