package com.sepbf.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sepbf.backend.mapper.ActionMapper;
import com.sepbf.backend.pojo.Action;
import com.sepbf.backend.service.ActionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("actionService")
public class ActionServiceImpl extends ServiceImpl<ActionMapper, Action> implements ActionService {
    @Resource
    private ActionMapper actionMapper;

    @Override
    public Integer getActionLimitByName(String name) {
        return actionMapper.selectLimitTimeByName(name);
    }

    @Override
    public int getActionIdByActionName(String item) {
        return actionMapper.selectIdByActionName(item);
    }
}
