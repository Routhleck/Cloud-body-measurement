package com.sepbf.backend.pojo.domain;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@ToString
public class TrainAction {
        private Integer count;
        private Double value;
        private String time;
        private String Name;
        private Integer practiceTime;
//        private Integer totalRecords;
//        private Integer totalPracticeTime;

        public TrainAction( Integer count, Double value, String time, String Name, Integer practiceTime) {
                this.count = count;
                this.value = value;
                this.time = time;
                this.Name = Name;
                this.practiceTime = practiceTime;
//                this.totalRecords = totalRecords;
//                this.totalPracticeTime = totalPracticeTime;
        }

        public Integer getPracticeTime() {
                return practiceTime;
        }
}
