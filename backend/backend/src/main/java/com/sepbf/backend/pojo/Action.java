package com.sepbf.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("action")
@Data
public class Action {

    @TableId
    private int action_id;

    private String name;

    private Integer limit_time;

}
