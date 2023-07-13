package com.cloudsports.actiondetect.api
import com.cloudsports.actiondetect.data.Grade
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface SportsService {
    @POST("/uploadTrainGrade")
    suspend fun updateGrade(
       @Body request : Grade.updateRequest
    ): Response<Map<String,Any>>

    @GET("/test")
    suspend fun updateGradeFouUserId(
        @Query("UserId") userId : Int
    ): Response<Map<String,Any>>

    @FormUrlEncoded
    @POST("/train/record")
    suspend fun getRecord(
        @Field ("UserId") userId: String
    ): Response<Map<String,Any>>


}