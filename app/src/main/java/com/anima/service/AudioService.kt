package com.anima.service

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AudioService(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var currentAudioUrl: String? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration.asStateFlow()

    private val _volume = MutableStateFlow(1.0f)
    val volume: StateFlow<Float> = _volume.asStateFlow()

    fun play(audioUrl: String) {
        if (currentAudioUrl == audioUrl && mediaPlayer?.isPlaying == true) {
            return
        }

        stop()
        currentAudioUrl = audioUrl

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, Uri.parse(audioUrl))
                setOnPreparedListener {
                    _duration.value = duration
                    start()
                    _isPlaying.value = true
                }
                setOnCompletionListener {
                    _isPlaying.value = false
                    _currentPosition.value = 0
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            // TODO: Обработка ошибок
        }
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _isPlaying.value = false
            }
        }
    }

    fun resume() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                _isPlaying.value = true
            }
        }
    }

    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
        currentAudioUrl = null
        _isPlaying.value = false
        _currentPosition.value = 0
        _duration.value = 0
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
        _currentPosition.value = position
    }

    fun setVolume(volume: Float) {
        mediaPlayer?.setVolume(volume, volume)
        _volume.value = volume
    }

    fun release() {
        stop()
    }
}

class AudioMixer(private val context: Context) {
    private val audioServices = mutableListOf<AudioService>()
    private var isPlaying = false

    fun addAudio(audioUrl: String): AudioService {
        val service = AudioService(context)
        audioServices.add(service)
        return service
    }

    fun removeAudio(service: AudioService) {
        service.stop()
        audioServices.remove(service)
    }

    fun playAll() {
        if (!isPlaying) {
            audioServices.forEach { it.resume() }
            isPlaying = true
        }
    }

    fun pauseAll() {
        if (isPlaying) {
            audioServices.forEach { it.pause() }
            isPlaying = false
        }
    }

    fun stopAll() {
        audioServices.forEach { it.stop() }
        audioServices.clear()
        isPlaying = false
    }

    fun setVolume(volume: Float) {
        audioServices.forEach { it.setVolume(volume) }
    }

    fun release() {
        stopAll()
    }
} 