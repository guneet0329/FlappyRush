package com.flappyrush.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class SoundManager(context: Context) {

    private val soundPool: SoundPool
    private val sounds = mutableMapOf<SoundEvent, Int>()

    enum class SoundEvent {
        FLAP, SCORE, HIT, DIE, WHOOSH
    }

    init {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(attrs)
            .build()

        // Load sounds from res/raw — add audio files in Phase 2
        // sounds[SoundEvent.FLAP]  = soundPool.load(context, R.raw.flap, 1)
        // sounds[SoundEvent.SCORE] = soundPool.load(context, R.raw.score, 1)
        // sounds[SoundEvent.HIT]   = soundPool.load(context, R.raw.hit, 1)
        // sounds[SoundEvent.DIE]   = soundPool.load(context, R.raw.die, 1)
    }

    fun play(event: SoundEvent) {
        sounds[event]?.let { soundPool.play(it, 1f, 1f, 0, 0, 1f) }
    }

    fun release() {
        soundPool.release()
    }
}
