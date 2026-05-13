package com.example.sromeromusicapp.network

import com.example.sromeromusicapp.model.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicApi {

    @GET("api/albums")
    suspend fun getAlbums(): List<Album>

    @GET("api/albums/{id}")
    suspend fun getAlbumDetail(
        @Path("id") id: Int
    ): Album
}