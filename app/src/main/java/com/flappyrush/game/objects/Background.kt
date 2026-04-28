package com.flappyrush.game.objects

import android.graphics.*
import kotlin.math.sin

class Background(private val screenWidth: Int, private val screenHeight: Int) {

    private val skyPaint = Paint()
    private val groundPaint = Paint()
    private val cloudPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val groundHeight = 80f

    // Cloud positions
    private val clouds = mutableListOf<Triple<Float, Float, Float>>() // x, y, scale
    private var scrollOffset = 0f
    private var time = 0f

    init {
        repeat(6) { i ->
            clouds.add(Triple(
                screenWidth * i / 5f + 60f,
                screenHeight * 0.1f + (i * 47f % (screenHeight * 0.3f)),
                0.7f + (i * 0.13f % 0.6f)
            ))
        }
    }

    fun update(speed: Float, deltaSeconds: Float) {
        scrollOffset += speed * 0.3f * deltaSeconds  // clouds scroll slower than pipes (parallax)
        time += deltaSeconds
        if (scrollOffset > screenWidth) scrollOffset -= screenWidth
    }

    fun draw(canvas: Canvas) {
        // Sky gradient (drawn as two rects to fake gradient without shader allocation every frame)
        skyPaint.color = Color.parseColor("#87CEEB")
        canvas.drawRect(0f, 0f, screenWidth.toFloat(), screenHeight * 0.6f, skyPaint)
        skyPaint.color = Color.parseColor("#B0E2FF")
        canvas.drawRect(0f, screenHeight * 0.6f, screenWidth.toFloat(), screenHeight - groundHeight, skyPaint)

        // Clouds
        cloudPaint.color = Color.argb(220, 255, 255, 255)
        clouds.forEach { (baseX, y, scale) ->
            val x = (baseX - scrollOffset + screenWidth) % screenWidth
            drawCloud(canvas, x, y, scale)
        }

        // Ground
        groundPaint.color = Color.parseColor("#8B6914")
        canvas.drawRect(0f, screenHeight - groundHeight, screenWidth.toFloat(), screenHeight.toFloat(), groundPaint)
        groundPaint.color = Color.parseColor("#5D8A3C")
        canvas.drawRect(0f, screenHeight - groundHeight, screenWidth.toFloat(), screenHeight - groundHeight + 18f, groundPaint)
    }

    private fun drawCloud(canvas: Canvas, cx: Float, cy: Float, scale: Float) {
        val r = 32f * scale
        canvas.drawCircle(cx, cy, r, cloudPaint)
        canvas.drawCircle(cx + r * 0.8f, cy + r * 0.2f, r * 0.75f, cloudPaint)
        canvas.drawCircle(cx - r * 0.7f, cy + r * 0.25f, r * 0.65f, cloudPaint)
        canvas.drawCircle(cx + r * 0.1f, cy + r * 0.5f, r * 0.9f, cloudPaint)
    }

    fun isGroundCollision(birdY: Float, birdHalfHeight: Float): Boolean {
        return birdY + birdHalfHeight >= screenHeight - groundHeight
    }
}
