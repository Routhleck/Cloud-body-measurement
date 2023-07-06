package com.sepbf.backend.controllerTest;

import com.sepbf.backend.mapper.TestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperSqlTest {
    private TestMapper testMapper;

    @Autowired
    public void setTestMapper(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    @Test
    public void getTest() {
        testMapper.selectById(1);
    }
}
