package com.cloudsports.actiondetect.netWorkUtils

import com.cloudsports.actiondetect.api.ApiService
import com.cloudsports.actiondetect.data.User
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UserLoginAPI {
    //创建retrofit对象
    private val retrofit =Retrofit.Builder()
        //设置服务器的基础URL
        .baseUrl("http://39.106.13.47:9090")
        // 添加 Gson 转换器，用于将服务器的响应转化为 Kotlin 对象
        .addConverterFactory(GsonConverterFactory.create())
        //构建retrofit对象
        .build()

    // 使用 Retrofit 创建 ApiService 接口的实例
    private val apiService = retrofit.create(ApiService::class.java)
    // 定义一个挂起函数 userLogin
    // 这个函数接收一个 User.LoginRequest 参数，返回一个可能为 null 的 JSONObject
    // 当这个函数被调用时，它会使用 apiService 发起一个网络请求
    // 网络请求的结果会被转化为一个 JSONObject 或者 null，然后返回
    suspend fun userLogin(request: User.LoginRequest): JSONObject? {
        // 使用 apiService 发起网络请求
        // 注意：因为 ApiService.userLogin 是一个挂起函数，所以这里不会阻塞线程
        val response = apiService.userLogin(request)
        // 检查响应是否成功
        // 如果成功，返回响应的 body（也就是服务器返回的数据）
        // 如果失败，返回 null
        return if (response.isSuccessful) {
            response.body()?.let { JSONObject(it) }
        } else {
            null
        }
    }


}

//fun main(){
//    val repository= UserLogin()
//    // 创建一个 User.LoginRequest 对象
//    val loginRequest = User.LoginRequest("admin", "admin")
//
//    // 使用 runBlocking 函数来在主线程中执行协程代码
//    runBlocking {
//        val result = repository.userLogin(loginRequest)
//
//        if (result != null) {
//            println("请求成功，返回结果：$result")
//        } else {
//            println("请求失败")
//        }
//    }
//
//}


