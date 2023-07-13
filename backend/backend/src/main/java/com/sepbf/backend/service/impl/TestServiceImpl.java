package com.sepbf.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sepbf.backend.mapper.TestMapper;
import com.sepbf.backend.pojo.Test;
import com.sepbf.backend.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("testService")
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

    @Resource
    private TestMapper testMapper;

    @Override
    public boolean addTest(Test test) {

        int result = testMapper.insert(test);
        return result == 0;
    }

    @Override
    public boolean deleteTest(Test test) {
        int result = testMapper.delete(test);
        return result == 0;
    }

    @Override
    public boolean updateTest(Test test) {
        int result = testMapper.updateById(test);
        return result == 0;
    }

    @Override
    public Test getTestById(Integer id) {
        return testMapper.selectById(id);
    }

    @Override
    public int getTestCountByIdAndYear(String id, int year) {
        return testMapper.getTestCountByIdAndYear(id, year);
    }


}
