package com.example.onppe_v1
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface EndPoint {


    @GET("getsignalements")
    suspend fun getsignalements():Response<List<Signalement>>

}