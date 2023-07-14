package com.cloudsports.actiondetect.netWorkUtils

import com.cloudsports.actiondetect.api.SportsService
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GradeAPI { //创建retrofit对象
    private val retrofit = Retrofit.Builder()
        //设置服务器的基础URL
        .baseUrl("http://39.106.13.47:9090")
        // 添加 Gson 转换器，用于将服务器的响应转化为 Kotlin 对象
        .addConverterFactory(GsonConverterFactory.create())
        //构建retrofit对象
        .build()

    // 使用 Retrofit 创建 ApiService 接口的实例
    private val sportService = retrofit.create(SportsService::class.java)
    // 定义一个挂起函数 upadate
    // 这个函数接收三个参数user_Id，item，number，返回一个可能为 null 的 JSONObject
    // 当这个函数被调用时，它会使用 sportsService 发起一个网络请求
    // 网络请求的结果会被转化为一个 JSONObject或者 null，然后返回
    suspend fun updateGrade(user_id:Int?,item:String,number:String,train_time:String): JSONObject? {
        // 使用 sportsService 发起网络请求
        // 注意：因为 sportsService.updateGrade 是一个挂起函数，所以这里不会阻塞线程
        val updateRequest=com.cloudsports.actiondetect.data.Grade.updateRequest(user_id,item,number,train_time)
        val response = sportService.updateGrade(updateRequest)
        // 检查响应是否成功
        // 如果成功，返回响应的 body（也就是服务器返回的数据）
        // 如果失败，返回 null
        return if (response.isSuccessful) {
            response.body()?.let { JSONObject(it) }
        } else {
            null
        }
    }
    suspend fun upadteGradeByUserId(user_id: Int):JSONObject?{
        val response = sportService.updateGradeFouUserId(user_id)
        return if (response.isSuccessful) {
            response.body()?.let { JSONObject(it) }
        } else {
            null
        }
    }
    suspend fun getRecord(user_id: Int):JSONObject?{
        val response = sportService.getRecord(user_id.toString())
        return if (response.isSuccessful) {
            response.body()?.let { JSONObject(it) }
        } else {
            null
        }
    }

}
//fun main(){
//    val repository= Grade()
////    // 创建一个 User.LoginRequest 对象
//    runBlocking {
//        val result = repository.upadteGradeByUserId(1)
//        println(result)
//    }
//}