package com.flappyrush.game.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import com.flappyrush.utils.Constants

class ScoreOverlay(context: Context, private val screenWidth: Int, private val screenHeight: Int) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("flappy_rush", Context.MODE_PRIVATE)

    private val bgPaint = Paint().apply { color = Color.argb(200, 20, 20, 40) }
    private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FF6B35")
        textSize = 64f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 96f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(200, 255, 255, 255)
        textSize = 36f
        textAlign = Paint.Align.CENTER
    }
    private val bestPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFD700")
        textSize = 48f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    fun saveBestScore(score: Int) {
        val best = prefs.getInt(Constants.BEST_SCORE_KEY, 0)
        if (score > best) prefs.edit().putInt(Constants.BEST_SCORE_KEY, score).apply()
    }

    fun getBestScore(): Int = prefs.getInt(Constants.BEST_SCORE_KEY, 0)

    fun draw(canvas: Canvas, score: Int) {
        val best = getBestScore()
        val cx = screenWidth / 2f
        val cy = screenHeight / 2f

        // Dark overlay panel
        val panelRect = RectF(cx - 260f, cy - 200f, cx + 260f, cy + 220f)
        canvas.drawRoundRect(panelRect, 24f, 24f, bgPaint)

        // Title
        canvas.drawText("GAME OVER", cx, cy - 130f, titlePaint)

        // Score
        canvas.drawText(score.toString(), cx, cy - 20f, scorePaint)
        canvas.drawText("SCORE", cx, cy + 30f, labelPaint)

        // Best
        if (score >= best && score > 0) {
            canvas.drawText("NEW BEST! 🎉", cx, cy + 90f, bestPaint)
        } else {
            canvas.drawText("BEST: $best", cx, cy + 90f, bestPaint)
        }

        // Tap to retry hint
        canvas.drawText("TAP TO PLAY AGAIN", cx, cy + 170f, labelPaint)
    }
}
