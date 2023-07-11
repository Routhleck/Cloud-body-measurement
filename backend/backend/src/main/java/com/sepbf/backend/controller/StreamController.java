package com.sepbf.backend.controller;

import okhttp3.RequestBody;
import org.springframework.web.bind.annotation.*;
import okhttp3.*;
import com.sepbf.backend.service.ActionService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

import com.sepbf.backend.pojo.domain.toFlask;

@RestController
@RequestMapping("/stream")
public class StreamController {

    @Resource
    private ActionService actionService;

    @PostMapping("/limitTime")
    public toFlask deliverToFlask (@org.springframework.web.bind.annotation.RequestBody Map<String,Object> map) throws IOException {
        System.out.println(map);
        Integer streamCode =Integer.parseInt( String.valueOf(map.get("streamCode")));
        String actionName = String.valueOf(map.get("actionName"));
        Integer limitTime = actionService.getActionLimitByName(actionName);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.format(
                "{\"streamCode\":\"%s\",\"actionName\":\"%s\",\"limitTime\":%d}",
                streamCode,
                actionName,
                limitTime
        ));
        toFlask flask = new toFlask(streamCode, actionName, limitTime);
//        Request request = new Request.Builder()
//                .url("http://
    return flask;
    }
}
