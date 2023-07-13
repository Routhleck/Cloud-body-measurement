package com.sepbf.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@TableName("test")
@Data
public class Test {

    private int user_id;

    private String test_time;

    private float height;

    private float weight;

    private int vital_capacity;

    private float standing_long_jump;

    private float sit_and_reach;

    private int pull_up;

    private float sprint_50m;

    private float long_distance_run;

    private int push_up;

    private int sit_up;

    private int squat;

    @TableId
    private int test_id;

    public Test(int userid, String test_time, float height, float weight, int vital_capacity, float standinglongjump, float sitandreach, int pullup, float sprint50M, float longdistancerun, int pushup, int situp, int squat, int testid) {
        user_id = userid;
        this.test_time = test_time;
        this.height = height;
        this.weight = weight;
        this.vital_capacity = vital_capacity;
        standing_long_jump = standinglongjump;
        sit_and_reach = sitandreach;
        pull_up = pullup;
        sprint_50m = sprint50M;
        long_distance_run = longdistancerun;
        push_up = pushup;
        sit_up = situp;
        this.squat = squat;
        test_id = testid;
    }

    public Test() {
    }
}
