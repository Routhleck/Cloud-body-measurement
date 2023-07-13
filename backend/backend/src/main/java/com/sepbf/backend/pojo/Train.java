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

        private DateTime Time;

        private int PracticeTime;

        private int Count;

        private float value;

        @TableId
        private int PracticeId;

        public Train( int UserId, int ActionId, DateTime Time, int PracticeTime, int Count, float value, int PracticeId) {
            this.UserId = UserId;
            this.ActionId = ActionId;
            this.Time = Time;
            this.PracticeTime = PracticeTime;
            this.Count = Count;
            this.value = value;
            this.PracticeId = PracticeId;
        }

}
