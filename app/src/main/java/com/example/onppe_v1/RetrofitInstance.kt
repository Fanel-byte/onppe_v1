package com.example.onppe_v1


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "your_base_url_here"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val endpoint: EndPoint by lazy {
        retrofit.create(EndPoint::class.java)
    }
}
