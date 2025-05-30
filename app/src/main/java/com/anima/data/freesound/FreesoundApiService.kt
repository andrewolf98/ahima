package com.anima.data.freesound

import retrofit2.http.GET
import retrofit2.http.Query

interface FreesoundApiService {

    @GET("search/text/")
    suspend fun searchSounds(
        @Query("query") query: String,
        @Query("token") token: String
        // Можно добавить другие параметры фильтрации, сортировки и пагинации
    ): FreesoundSearchResult
} 