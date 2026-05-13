package com.example.sromeromusicapp.model

import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumApi {
    @GET("api/albums")
    suspend fun getAlbums(): List<Album>

    @GET("api/albums/{id}")
    suspend fun getAlbumById(@Path("id") id: String): Album
}