package com.sepbf.backend.controller;

import com.sepbf.backend.utils.FaceSecretKey;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

@RestController
public class AuthController {
    public static final String API_KEY = FaceSecretKey.API_KEY;
    public static final String SECRET_KEY = FaceSecretKey.SECRET_KEY;
    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    @PostMapping("/auth")
    public String auth(@RequestParam("userId") Integer userId) throws IOException {
        String userImagedir= "https://tcloud-1318685426.cos.ap-beijing.myqcloud.com/action%2FuserImages%2F" + userId.toString() + ".png";
        String checkImagedir= "https://tcloud-1318685426.cos.ap-beijing.myqcloud.com/action%2FcheckImages%2F" + userId + ".png";
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.format(
                "[{\"image\":\"%s\",\"image_type\":\"URL\"},{\"image\":\"%s\",\"image_type\":\"URL\"}]",
                userImagedir,
                checkImagedir
        ));
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/face/v3/match?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);
        return responseBody;

    }

    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }
}
