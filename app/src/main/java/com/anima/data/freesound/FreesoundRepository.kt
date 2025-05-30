package com.anima.data.freesound

import com.anima.data.Result
import com.anima.data.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.anima.BuildConfig

class FreesoundRepository(private val apiService: FreesoundApiService) {

    // Используем Freesound API Key из BuildConfig
    private val apiKey: String = BuildConfig.FREESOUND_CLIENT_ID

    fun searchSounds(query: String): Flow<Result<List<FreesoundSound>>> = flow {
        emit(Result.Loading)
        try {
            val result = apiService.searchSounds(query, apiKey)
            emit(Result.Success(result.results))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.asResult()
} 