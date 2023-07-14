package com.sepbf.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sepbf.backend.pojo.Test;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TestService extends IService<Test>{
    boolean addTest(Test test);

    boolean deleteTest(Test test);

    boolean updateTest(Test test);

    List<Test> getTestById(Integer id);

    Test getTestByIdAndYear(Integer id, Integer year);

    int getTestCountByIdAndYear(String id, int year);
}
