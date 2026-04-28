package com.flappyrush.game.ui

import android.graphics.*

class HUD(private val screenWidth: Int, private val screenHeight: Int) {

    private var displayScore = 0
    private var targetScore = 0
    private var scoreScale = 1f         // pops on score
    private var scoreAlpha = 255

    private val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        setShadowLayer(8f, 3f, 4f, Color.argb(160, 0, 0, 0))
    }
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 34f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        setShadowLayer(4f, 2f, 2f, Color.argb(120, 0, 0, 0))
    }
    private val tapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 38f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        setShadowLayer(6f, 2f, 3f, Color.argb(140, 0, 0, 0))
    }
    private val pillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(100, 0, 0, 0)
    }

    fun onScore(newScore: Int) {
        targetScore = newScore
        scoreScale = 1.4f   // trigger pop animation
    }

    fun update(deltaSeconds: Float) {
        // Animate score pop
        if (scoreScale > 1f) scoreScale = (scoreScale - deltaSeconds * 3f).coerceAtLeast(1f)
        // Smooth score count-up
        if (displayScore < targetScore) displayScore++
    }

    fun draw(canvas: Canvas, score: Int) {
        targetScore = score
        val cx = screenWidth / 2f

        // Pill background behind score
        val pillRect = RectF(cx - 80f, 60f, cx + 80f, 150f)
        canvas.drawRoundRect(pillRect, 40f, 40f, pillPaint)

        // Score number with pop scale
        scorePaint.textSize = 88f * scoreScale
        scorePaint.color = Color.WHITE
        canvas.drawText(displayScore.toString(), cx, 138f, scorePaint)
    }

    fun drawMenu(canvas: Canvas) {
        val cx = screenWidth / 2f
        val cy = screenHeight / 2f

        // Title
        scorePaint.textSize = 72f
        scorePaint.color = Color.parseColor("#FFD700")
        canvas.drawText("FLAPPY RUSH", cx, cy - 80f, scorePaint)

        // Subtitle
        labelPaint.color = Color.parseColor("#FFD700")
        canvas.drawText("COMPETITIVE EDITION", cx, cy - 40f, labelPaint)

        // Tap prompt (pulsing)
        tapPaint.color = Color.WHITE
        canvas.drawText("TAP TO START", cx, cy + 60f, tapPaint)
    }

    fun drawCountdown(canvas: Canvas, count: Int) {
        scorePaint.textSize = 120f
        scorePaint.color = Color.WHITE
        canvas.drawText(if (count > 0) count.toString() else "GO!", screenWidth / 2f, screenHeight / 2f, scorePaint)
    }
}
