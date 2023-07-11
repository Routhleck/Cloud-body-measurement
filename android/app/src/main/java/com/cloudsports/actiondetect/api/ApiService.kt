package com.cloudsports.actiondetect.api

import com.cloudsports.actiondetect.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/user/login")
    suspend fun userlogin(@Body request: User.LoginRequest):Response<Map<String,Any>>

    @POST("user/register")
    suspend fun userRegister(@Body request: User.RegisterRequest)
}