package com.example.onppe_v1
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface Endpoint {


    @Multipart
    @POST("images/add")
    suspend fun addImg(@Part img : MultipartBody.Part,@Part image: MultipartBody.Part): Response<Int>


    @POST("signalement/create")
    suspend fun addSignalement(@Body signalement: Signalement) : Response<Int>

    @Multipart
    @POST("videos/add")
    suspend fun addVideo(@Part videoInfo : MultipartBody.Part,@Part video: MultipartBody.Part): Response<Int>
    @Multipart
    @POST("vocaux/add")
    suspend fun addSon(@Part sonInfo : MultipartBody.Part,@Part son: MultipartBody.Part): Response<Int>

    @POST("/enfants/create")
    suspend fun createEnfant(@Body enfant: Enfant): Response<Int>

    @POST("/citoyen/create")
    suspend fun createCitoyen(@Body citoyen: Citoyen): Response<Int>

}