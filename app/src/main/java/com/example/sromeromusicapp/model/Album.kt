package com.example.sromeromusicapp.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("_id", alternate = ["id"]) val id: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("artist") val artist: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("image") val image: String? = null
) {
    fun getFullImageUrl(): String {
        val seed = (id ?: title ?: "album").replace(" ", "").take(20)
        return "https://picsum.photos/seed/$seed/400/400"
    }
}
