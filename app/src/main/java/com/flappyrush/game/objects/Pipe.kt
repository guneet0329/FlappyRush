package com.flappyrush.game.objects

import android.graphics.*
import com.flappyrush.utils.Constants

class Pipe(
    var x: Float,
    private val gapCenterY: Float,
    private val screenHeight: Int
) {
    var passed: Boolean = false

    private val topRect = RectF()
    private val bottomRect = RectF()
    private val topCapRect = RectF()
    private val bottomCapRect = RectF()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val capPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Colors — Phase 2 will bring gradient & texture
    private val pipeColor = Color.parseColor("#4CAF50")
    private val capColor  = Color.parseColor("#388E3C")
    private val rimColor  = Color.parseColor("#2E7D32")

    private val halfGap = Constants.PIPE_GAP / 2f
    private val capHeight = 30f
    private val capOverhang = 10f

    val topHitbox: RectF get() = RectF(x, 0f, x + Constants.PIPE_WIDTH, gapCenterY - halfGap)
    val bottomHitbox: RectF get() = RectF(x, gapCenterY + halfGap, x + Constants.PIPE_WIDTH, screenHeight.toFloat())

    fun update(speed: Float, deltaSeconds: Float) {
        x -= speed * deltaSeconds
    }

    fun isOffScreen(): Boolean = x + Constants.PIPE_WIDTH < 0f

    fun draw(canvas: Canvas) {
        val topBottom = gapCenterY - halfGap
        val bottomTop = gapCenterY + halfGap

        // Top pipe body
        paint.color = pipeColor
        topRect.set(x, -10f, x + Constants.PIPE_WIDTH, topBottom - capHeight)
        canvas.drawRect(topRect, paint)

        // Top pipe cap
        capPaint.color = capColor
        topCapRect.set(x - capOverhang, topBottom - capHeight, x + Constants.PIPE_WIDTH + capOverhang, topBottom)
        canvas.drawRoundRect(topCapRect, 8f, 8f, capPaint)

        // Top cap rim (darker line)
        paint.color = rimColor
        paint.strokeWidth = 3f
        paint.style = Paint.Style.STROKE
        canvas.drawRoundRect(topCapRect, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        // Bottom pipe body
        paint.color = pipeColor
        bottomRect.set(x, bottomTop + capHeight, x + Constants.PIPE_WIDTH, screenHeight + 10f)
        canvas.drawRect(bottomRect, paint)

        // Bottom pipe cap
        capPaint.color = capColor
        bottomCapRect.set(x - capOverhang, bottomTop, x + Constants.PIPE_WIDTH + capOverhang, bottomTop + capHeight)
        canvas.drawRoundRect(bottomCapRect, 8f, 8f, capPaint)

        // Bottom cap rim
        paint.color = rimColor
        paint.style = Paint.Style.STROKE
        canvas.drawRoundRect(bottomCapRect, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        // Pipe highlight (shine effect)
        paint.color = Color.argb(40, 255, 255, 255)
        canvas.drawRect(x + 8f, -10f, x + 22f, topBottom - capHeight, paint)
        canvas.drawRect(x + 8f, bottomTop + capHeight, x + 22f, screenHeight + 10f, paint)
    }
}
