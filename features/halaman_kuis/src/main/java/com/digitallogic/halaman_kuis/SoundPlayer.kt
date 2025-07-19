package com.digitallogic.halaman_kuis

import android.content.Context
import android.media.MediaPlayer

object SoundPlayer {
    private var mediaPlayer: MediaPlayer? = null
    var isSoundOn: Boolean = true

    fun playSound(context: Context, resId: Int) {
        if (!isSoundOn) return

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.start()
    }

    fun toggleSound(): Boolean {
        isSoundOn = !isSoundOn
        return isSoundOn
    }
}