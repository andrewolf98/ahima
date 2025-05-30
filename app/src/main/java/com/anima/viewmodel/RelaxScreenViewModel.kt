package com.anima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anima.data.freesound.FreesoundApiClient
import com.anima.data.freesound.FreesoundRepository
import com.anima.data.freesound.FreesoundSound
import com.anima.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RelaxScreenViewModel(private val freesoundRepository: FreesoundRepository) : ViewModel() {

    private val _sounds = MutableStateFlow<Result<List<FreesoundSound>>>(Result.Loading)
    val sounds: StateFlow<Result<List<FreesoundSound>>> = _sounds.asStateFlow()

    init {
        // При инициализации ViewModel загружаем звуки по умолчанию
        searchSounds("nature")
    }

    fun searchSounds(query: String) {
        viewModelScope.launch {
            freesoundRepository.searchSounds(query).collect {
                _sounds.value = it
            }
        }
    }

    // TODO: Добавить функции для управления медитациями, дыхательными техниками и миксами
    // TODO: Добавить логику для автоматических рекомендаций
} 