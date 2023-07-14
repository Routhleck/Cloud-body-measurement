package com.sepbf.backend.pojo.domain;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@ToString
public class toFlask {
    private Integer streamCode;
    private String actionName;
    private Integer limitTime;

    public toFlask(Integer streamCode, String actionName, Integer limitTime) {
        this.streamCode = streamCode;
        this.actionName = actionName;
        this.limitTime = limitTime;
    }
}
