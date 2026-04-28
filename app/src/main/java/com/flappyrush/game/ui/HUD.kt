package com.flappyrush.game.ui

import android.graphics.*

class HUD(private val screenWidth: Int) {

    private val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 96f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        setShadowLayer(6f, 3f, 3f, Color.argb(160, 0, 0, 0))
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(200, 255, 255, 255)
        textSize = 32f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    fun draw(canvas: Canvas, score: Int) {
        canvas.drawText(score.toString(), screenWidth / 2f, 160f, scorePaint)
    }

    fun drawTapToStart(canvas: Canvas, screenHeight: Int) {
        labelPaint.color = Color.WHITE
        canvas.drawText("TAP TO FLAP", screenWidth / 2f, screenHeight * 0.65f, labelPaint)
    }
}
