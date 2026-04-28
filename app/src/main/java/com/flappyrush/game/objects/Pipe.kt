package com.flappyrush.game.objects

import android.graphics.*
import com.flappyrush.game.theme.PipeTheme
import com.flappyrush.utils.Constants

class Pipe(
    var x: Float,
    private val gapCenterY: Float,
    private val screenHeight: Int,
    var theme: PipeTheme = PipeTheme.CLASSIC
) {
    var passed: Boolean = false

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val halfGap = Constants.PIPE_GAP / 2f
    private val capHeight = 34f
    private val capOverhang = 12f

    val topHitbox: RectF get() = RectF(x + 4f, 0f, x + Constants.PIPE_WIDTH - 4f, gapCenterY - halfGap)
    val bottomHitbox: RectF get() = RectF(x + 4f, gapCenterY + halfGap, x + Constants.PIPE_WIDTH - 4f, screenHeight.toFloat())

    fun update(speed: Float, deltaSeconds: Float) {
        x -= speed * deltaSeconds
    }

    fun isOffScreen(): Boolean = x + Constants.PIPE_WIDTH < 0f

    fun draw(canvas: Canvas) {
        val topBottom = gapCenterY - halfGap
        val bottomTop = gapCenterY + halfGap

        drawPipeBody(canvas, x, -20f, x + Constants.PIPE_WIDTH, topBottom - capHeight)
        drawPipeCap(canvas, x - capOverhang, topBottom - capHeight, x + Constants.PIPE_WIDTH + capOverhang, topBottom)
        drawPipeBody(canvas, x, bottomTop + capHeight, x + Constants.PIPE_WIDTH, screenHeight + 20f)
        drawPipeCap(canvas, x - capOverhang, bottomTop, x + Constants.PIPE_WIDTH + capOverhang, bottomTop + capHeight)

        // Highlight shine on both pipes
        paint.color = Color.argb(50, 255, 255, 255)
        canvas.drawRect(x + 10f, -20f, x + 26f, topBottom - capHeight, paint)
        canvas.drawRect(x + 10f, bottomTop + capHeight, x + 26f, screenHeight + 20f, paint)
    }

    private fun drawPipeBody(canvas: Canvas, l: Float, t: Float, r: Float, b: Float) {
        paint.color = theme.bodyColor
        paint.style = Paint.Style.FILL
        canvas.drawRect(l, t, r, b, paint)

        // Dark edge shading (right side)
        paint.color = Color.argb(60, 0, 0, 0)
        canvas.drawRect(r - 14f, t, r, b, paint)
    }

    private fun drawPipeCap(canvas: Canvas, l: Float, t: Float, r: Float, b: Float) {
        // Cap fill
        paint.color = theme.capColor
        paint.style = Paint.Style.FILL
        val rect = RectF(l, t, r, b)
        canvas.drawRoundRect(rect, 10f, 10f, paint)

        // Cap rim
        paint.color = theme.rimColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.5f
        canvas.drawRoundRect(rect, 10f, 10f, paint)
        paint.style = Paint.Style.FILL

        // Cap highlight
        paint.color = Color.argb(60, 255, 255, 255)
        canvas.drawRoundRect(RectF(l + 6f, t + 4f, r - 6f, t + 14f), 6f, 6f, paint)
    }
}
