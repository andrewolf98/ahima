package com.example.ahima.audio

import android.content.Context
import android.media.MediaPlayer

object SoundPlayer {
    private var mediaPlayer: MediaPlayer? = null

    fun play(context: Context, assetPath: String) {
        stop()
        val afd = context.assets.openFd(assetPath)
        mediaPlayer = MediaPlayer().apply {
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            prepare()
            start()
        }
    }

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
