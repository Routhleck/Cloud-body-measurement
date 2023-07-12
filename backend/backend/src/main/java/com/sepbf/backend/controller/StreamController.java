package com.sepbf.backend.controller;

import com.sepbf.backend.pojo.domain.toFlask;
import com.sepbf.backend.service.ActionService;
import com.sepbf.backend.utils.Result;
import okhttp3.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/stream")
public class StreamController {
    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    @Resource
    private ActionService actionService;

    @PostMapping("/limitTime")
    public Result deliverToFlask (@org.springframework.web.bind.annotation.RequestBody Map<String,Object> map) throws IOException {
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
        Request request = new Request.Builder()
                .url("http://127.0.0.1:5000/exercise")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);
    return Result.success(responseBody);
    }
}
