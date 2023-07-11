package com.sepbf.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sepbf.backend.pojo.Action;
import org.springframework.stereotype.Component;

@Component
public interface ActionService extends IService<Action>{

    Integer getActionLimitByName(String name);

}
