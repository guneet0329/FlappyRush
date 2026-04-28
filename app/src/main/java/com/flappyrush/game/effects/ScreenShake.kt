package com.flappyrush.game.effects

import android.graphics.Canvas
import kotlin.math.sin
import kotlin.random.Random

class ScreenShake {

    private var intensity = 0f
    private var duration = 0f
    private var elapsed = 0f
    private var offsetX = 0f
    private var offsetY = 0f

    fun shake(intensity: Float, duration: Float) {
        this.intensity = intensity
        this.duration = duration
        this.elapsed = 0f
    }

    fun update(deltaSeconds: Float) {
        if (elapsed >= duration) {
            offsetX = 0f
            offsetY = 0f
            return
        }
        elapsed += deltaSeconds
        val progress = 1f - (elapsed / duration)
        val currentIntensity = intensity * progress
        offsetX = (Random.nextFloat() * 2f - 1f) * currentIntensity
        offsetY = (Random.nextFloat() * 2f - 1f) * currentIntensity
    }

    fun apply(canvas: Canvas) {
        if (elapsed < duration) canvas.translate(offsetX, offsetY)
    }

    val isActive get() = elapsed < duration
}
