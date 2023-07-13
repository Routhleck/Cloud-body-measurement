package com.sepbf.backend.controller;

import com.sepbf.backend.service.ActionService;
import com.sepbf.backend.utils.Result;
import okhttp3.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/stream")
public class StreamController {
    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .readTimeout(360, TimeUnit.SECONDS)
            .writeTimeout(360, TimeUnit.SECONDS)
            .connectTimeout(360, TimeUnit.SECONDS)
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

        Request request = new Request.Builder()
                .url("http://39.106.13.47:5000/exercise")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);
    return Result.success(responseBody);
    }

    @PostMapping("/testTime")
    public Result testTime (@org.springframework.web.bind.annotation.RequestBody Map<String,Object> map) throws IOException {
        System.out.println(map);
        Integer streamCode =Integer.parseInt( String.valueOf(map.get("streamCode")));
        String actionName = String.valueOf(map.get("actionName"));
        Integer limitTime = Integer.parseInt( String.valueOf(map.get("limitTime")));
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.format(
                "{\"streamCode\":\"%s\",\"actionName\":\"%s\",\"limitTime\":%d}",
                streamCode,
                actionName,
                limitTime
        ));

        Request request = new Request.Builder()
                .url("http://39.106.13.47:5000/exercise")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);
        return Result.success(responseBody);
    }
}
