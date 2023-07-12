package com.sepbf.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;


@TableName("test")
@Data
public class Test {

    private int UserId;

    private Date TestTime;

    private float Height;

    private float Weight;

    private int VitalCapacity;

    private float StandingLongJump;

    private float SitAndReach;

    private int PullOrSitUp;

    private float Sprint50m;

    private float LongDistanceRun;

    @TableId
    private int TestId;

    public Test (int UserId,Date TestTime, float Height, float Weight, int VitalCapacity, float StandingLongJump, float SitAndReach, int PullOrSitUp, float Sprint50m, float LongDistanceRun,int TestId) {
        this.UserId = UserId;
        this.TestTime = TestTime;
        this.Height = Height;
        this.Weight = Weight;
        this.VitalCapacity = VitalCapacity;
        this.StandingLongJump = StandingLongJump;
        this.SitAndReach = SitAndReach;
        this.PullOrSitUp = PullOrSitUp;
        this.Sprint50m = Sprint50m;
        this.LongDistanceRun = LongDistanceRun;
        this.TestId = TestId;
    }

}
