package com.flappyrush.game.objects

import android.graphics.*
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

class Background(private val screenWidth: Int, private val screenHeight: Int) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val groundHeight = 90f

    // Parallax layers (speed multiplier relative to pipe speed)
    private var farOffset = 0f       // mountains — 0.1x
    private var midOffset = 0f       // hills — 0.2x
    private var nearOffset = 0f      // bushes — 0.4x

    // Day/night cycle
    private var timeOfDay = 0f       // 0-1, loops
    private val dayDuration = 60f    // seconds for full cycle

    // Stars
    private val stars = List(80) { Triple(
        Random.nextFloat() * screenWidth,
        Random.nextFloat() * screenHeight * 0.7f,
        Random.nextFloat() * 3f + 1f   // radius
    )}

    // Cloud data (x, y, scale, speed factor)
    private val clouds = List(7) { i -> floatArrayOf(
        screenWidth * i / 6f + 80f,
        screenHeight * 0.08f + (i * 53f % (screenHeight * 0.28f)),
        0.6f + (i * 0.12f % 0.7f),
        0.15f + (i * 0.03f % 0.1f)
    )}

    fun update(pipeSpeed: Float, deltaSeconds: Float) {
        farOffset  = (farOffset  + pipeSpeed * 0.08f * deltaSeconds) % screenWidth
        midOffset  = (midOffset  + pipeSpeed * 0.18f * deltaSeconds) % screenWidth
        nearOffset = (nearOffset + pipeSpeed * 0.38f * deltaSeconds) % screenWidth
        timeOfDay  = (timeOfDay  + deltaSeconds / dayDuration) % 1f
    }

    // Sky colors for day/night interpolation
    private fun skyTop(): Int {
        val t = sin(timeOfDay * Math.PI.toFloat() * 2)
        return when {
            t > 0.3f  -> Color.parseColor("#1565C0")   // deep blue day
            t > -0.3f -> Color.parseColor("#4A148C")   // purple dusk/dawn
            else      -> Color.parseColor("#0D0D1A")   // dark night
        }
    }
    private fun skyBottom(): Int {
        val t = sin(timeOfDay * Math.PI.toFloat() * 2)
        return when {
            t > 0.3f  -> Color.parseColor("#42A5F5")   // light blue
            t > -0.3f -> Color.parseColor("#FF7043")   // orange horizon
            else      -> Color.parseColor("#1A237E")   // dark blue night
        }
    }

    fun draw(canvas: Canvas) {
        val isNight = sin(timeOfDay * Math.PI.toFloat() * 2) < -0.1f

        // Sky (two-tone fake gradient)
        paint.color = skyTop()
        canvas.drawRect(0f, 0f, screenWidth.toFloat(), screenHeight * 0.5f, paint)
        paint.color = skyBottom()
        canvas.drawRect(0f, screenHeight * 0.5f, screenWidth.toFloat(), screenHeight - groundHeight, paint)

        // Stars at night
        if (isNight) {
            paint.color = Color.WHITE
            stars.forEach { (sx, sy, sr) ->
                val twinkle = abs(sin(timeOfDay * 30f + sx)) * 0.5f + 0.5f
                paint.alpha = (twinkle * 200).toInt()
                canvas.drawCircle(sx, sy, sr, paint)
            }
            paint.alpha = 255
        }

        // Sun or Moon
        if (!isNight) drawSun(canvas) else drawMoon(canvas)

        // Far mountains
        drawMountains(canvas, farOffset, screenHeight * 0.55f, 0.18f,
            if (isNight) Color.parseColor("#1A237E") else Color.parseColor("#5C6BC0"))

        // Mid hills
        drawMountains(canvas, midOffset, screenHeight * 0.65f, 0.12f,
            if (isNight) Color.parseColor("#1B5E20") else Color.parseColor("#388E3C"))

        // Clouds
        paint.alpha = if (isNight) 60 else 200
        paint.color = Color.WHITE
        clouds.forEach { cloud ->
            val cx = (cloud[0] - midOffset * cloud[3] * 6f + screenWidth * 2) % (screenWidth + 200f) - 100f
            drawCloud(canvas, cx, cloud[1], cloud[2])
        }
        paint.alpha = 255

        // Near bushes
        drawBushes(canvas, nearOffset,
            if (isNight) Color.parseColor("#1B5E20") else Color.parseColor("#2E7D32"))

        // Ground
        paint.color = if (isNight) Color.parseColor("#4E342E") else Color.parseColor("#6D4C41")
        canvas.drawRect(0f, screenHeight - groundHeight, screenWidth.toFloat(), screenHeight.toFloat(), paint)

        // Grass strip
        paint.color = if (isNight) Color.parseColor("#2E7D32") else Color.parseColor("#43A047")
        canvas.drawRect(0f, screenHeight - groundHeight, screenWidth.toFloat(), screenHeight - groundHeight + 20f, paint)

        // Ground line detail
        paint.color = Color.argb(40, 0, 0, 0)
        canvas.drawRect(0f, screenHeight - groundHeight + 20f, screenWidth.toFloat(), screenHeight - groundHeight + 23f, paint)
    }

    private fun drawSun(canvas: Canvas) {
        val sunX = screenWidth * 0.8f
        val sunY = screenHeight * 0.12f
        // Glow
        paint.color = Color.argb(40, 255, 235, 59)
        canvas.drawCircle(sunX, sunY, 55f, paint)
        paint.color = Color.argb(80, 255, 235, 59)
        canvas.drawCircle(sunX, sunY, 40f, paint)
        // Core
        paint.color = Color.parseColor("#FFF176")
        canvas.drawCircle(sunX, sunY, 28f, paint)
    }

    private fun drawMoon(canvas: Canvas) {
        val mx = screenWidth * 0.75f
        val my = screenHeight * 0.1f
        paint.color = Color.parseColor("#E8EAF6")
        canvas.drawCircle(mx, my, 26f, paint)
        // Crescent shadow
        paint.color = skyTop()
        canvas.drawCircle(mx + 10f, my - 6f, 22f, paint)
    }

    private fun drawMountains(canvas: Canvas, offset: Float, baseY: Float, amplitude: Float, color: Int) {
        paint.color = color
        val path = Path()
        val w = screenWidth.toFloat()
        path.moveTo(-offset % w - 10f, screenHeight - groundHeight)
        var px = -offset % w
        while (px < screenWidth + 200f) {
            val peakH = screenHeight * amplitude + (px * 0.3f % (screenHeight * 0.08f))
            path.lineTo(px, baseY - peakH)
            path.lineTo(px + 80f, baseY)
            px += 160f
        }
        path.lineTo(screenWidth + 10f, screenHeight - groundHeight)
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawBushes(canvas: Canvas, offset: Float, color: Int) {
        paint.color = color
        var bx = -(offset % 200f)
        while (bx < screenWidth + 100f) {
            val by = screenHeight - groundHeight
            canvas.drawCircle(bx, by, 28f, paint)
            canvas.drawCircle(bx + 22f, by + 5f, 20f, paint)
            canvas.drawCircle(bx - 18f, by + 8f, 16f, paint)
            bx += 180f
        }
    }

    private fun drawCloud(canvas: Canvas, cx: Float, cy: Float, scale: Float) {
        val r = 34f * scale
        canvas.drawCircle(cx, cy, r, paint)
        canvas.drawCircle(cx + r * 0.85f, cy + r * 0.25f, r * 0.72f, paint)
        canvas.drawCircle(cx - r * 0.72f, cy + r * 0.28f, r * 0.62f, paint)
        canvas.drawCircle(cx + r * 0.12f, cy + r * 0.52f, r * 0.88f, paint)
    }

    fun isGroundCollision(birdY: Float, birdHalfHeight: Float): Boolean {
        return birdY + birdHalfHeight >= screenHeight - groundHeight
    }

    val groundY get() = screenHeight - groundHeight
}
