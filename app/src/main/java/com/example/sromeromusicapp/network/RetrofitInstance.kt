package com.example.sromeromusicapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: MusicApi by lazy {

        Retrofit.Builder()
            .baseUrl("https://musicapi.pjasoft.com/")
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(MusicApi::class.java)
    }
}