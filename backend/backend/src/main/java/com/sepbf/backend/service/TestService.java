package com.sepbf.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sepbf.backend.pojo.Test;
import org.springframework.stereotype.Component;

@Component
public interface TestService extends IService<Test>{
    boolean addTest(Test test);

    boolean deleteTest(Test test);

    boolean updateTest(Test test);

    Test getTestById(Integer id);

    int getTestCountByIdAndYear(String id, int year);
}
