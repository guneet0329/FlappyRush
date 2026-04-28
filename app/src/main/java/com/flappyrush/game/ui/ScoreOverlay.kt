package com.flappyrush.game.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import com.flappyrush.utils.Constants

class ScoreOverlay(context: Context, private val screenWidth: Int, private val screenHeight: Int) {

    private val prefs: SharedPreferences = context.getSharedPreferences("flappy_rush", Context.MODE_PRIVATE)

    private val dimPaint = Paint().apply { color = Color.argb(160, 0, 0, 20) }
    private val panelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#1A1A2E") }
    private val accentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#FF6B35") }
    private val dividerPaint = Paint().apply {
        color = Color.argb(60, 255, 255, 255)
        strokeWidth = 1f
        style = Paint.Style.STROKE
    }

    private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FF6B35")
        textSize = 58f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        setShadowLayer(6f, 0f, 3f, Color.argb(100, 255, 100, 50))
    }
    private val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 100f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(160, 255, 255, 255)
        textSize = 28f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val bestPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFD700")
        textSize = 52f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        setShadowLayer(4f, 0f, 2f, Color.argb(120, 200, 150, 0))
    }
    private val hintPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(180, 255, 255, 255)
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }
    private val newBestPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFD700")
        textSize = 32f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    fun saveBestScore(score: Int) {
        val best = prefs.getInt(Constants.BEST_SCORE_KEY, 0)
        if (score > best) prefs.edit().putInt(Constants.BEST_SCORE_KEY, score).apply()
    }

    fun getBestScore(): Int = prefs.getInt(Constants.BEST_SCORE_KEY, 0)

    fun isNewBest(score: Int): Boolean = score > 0 && score >= getBestScore()

    fun draw(canvas: Canvas, score: Int) {
        val best = getBestScore()
        val cx = screenWidth / 2f
        val cy = screenHeight / 2f
        val panelW = 300f
        val panelH = 340f

        // Full screen dim
        canvas.drawRect(0f, 0f, screenWidth.toFloat(), screenHeight.toFloat(), dimPaint)

        // Panel
        val panelRect = RectF(cx - panelW, cy - panelH / 2f, cx + panelW, cy + panelH / 2f)
        canvas.drawRoundRect(panelRect, 28f, 28f, panelPaint)

        // Top accent bar
        val accentRect = RectF(cx - panelW, cy - panelH / 2f, cx + panelW, cy - panelH / 2f + 8f)
        canvas.drawRoundRect(accentRect, 28f, 28f, accentPaint)

        // GAME OVER title
        canvas.drawText("GAME OVER", cx, cy - panelH / 2f + 70f, titlePaint)

        // Score
        canvas.drawText(score.toString(), cx, cy - 10f, scorePaint)
        canvas.drawText("SCORE", cx, cy + 30f, labelPaint)

        // Divider
        canvas.drawLine(cx - 180f, cy + 55f, cx + 180f, cy + 55f, dividerPaint)

        // Best score row
        if (isNewBest(score)) {
            newBestPaint.color = Color.parseColor("#FFD700")
            canvas.drawText("★  NEW BEST!  ★", cx, cy + 105f, newBestPaint)
        } else {
            canvas.drawText("BEST  $best", cx, cy + 105f, bestPaint)
        }

        // Tap to retry
        canvas.drawText("tap to play again", cx, cy + panelH / 2f - 30f, hintPaint)
    }
}
