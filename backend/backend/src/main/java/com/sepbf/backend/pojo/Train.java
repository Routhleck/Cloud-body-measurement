package com.sepbf.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.Date;

@TableName("train")
@Data
public class Train {

        private int UserId;

        private int ActionId;

        private String Time;

        private Integer PracticeTime;

        private Integer Count;

        private double value;

        @TableId
        private int PracticeId;

        public Train( int UserId, int ActionId, String Time, Integer PracticeTime, Integer Count, double value, int PracticeId) {
            this.UserId = UserId;
            this.ActionId = ActionId;
            this.Time = Time;
            this.PracticeTime = PracticeTime;
            this.Count = Count;
            this.value = value;
            this.PracticeId = PracticeId;
        }

}
