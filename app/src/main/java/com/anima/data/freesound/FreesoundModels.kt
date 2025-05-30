package com.anima.data.freesound

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FreesoundSound(
    val id: Int,
    val name: String,
    val tags: List<String>,
    val description: String?,
    val username: String,
    val previews: Previews
)

@JsonClass(generateAdapter = true)
data class Previews(
    val previewLqOgg: String,
    val previewLqMp3: String,
    val previewMqOgg: String,
    val previewMqMp3: String,
    val previewHqOgg: String,
    val previewHqMp3: String
)

@JsonClass(generateAdapter = true)
data class FreesoundSearchResult(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<FreesoundSound>
) 