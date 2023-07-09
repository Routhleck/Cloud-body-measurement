package com.example.demo;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class FaceRec {

    public static final String API_KEY = FaceSecretKey.API_KEY;
    public static final String SECRET_KEY = FaceSecretKey.SECRET_KEY;

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static void main(String[] args) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "[{\"image\":\"https://avatars.githubusercontent.com/u/124519890?v=4\",\"image_type\":\"URL\"},{\"image\":\"https://avatars.githubusercontent.com/u/88108241?v=4\",\"image_type\":\"URL\"}]");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/face/v3/match?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        System.out.println(response.body().string());

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