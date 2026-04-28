package com.flappyrush.game.competitive

import android.graphics.*
import com.flappyrush.utils.Constants

class GhostBird(private val screenWidth: Int, screenHeight: Int) {

    var x: Float = screenWidth * Constants.BIRD_START_X_RATIO
    var y: Float = screenHeight / 2f
    var isActive: Boolean = false
    var opponentScore: Int = 0

    // Smooth interpolation
    private var targetY: Float = y
    private val smoothing = 12f

    private val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(Constants.GHOST_ALPHA, 100, 180, 255)
    }
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(40, 100, 180, 255)
    }
    private val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(180, 100, 200, 255)
        textSize = 28f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val bodyRect = RectF()
    private val glowRect = RectF()

    fun receivePosition(newY: Float, score: Int) {
        targetY = newY
        opponentScore = score
    }

    fun update(deltaSeconds: Float) {
        if (!isActive) return
        // Lerp toward target for smooth movement
        y += (targetY - y) * smoothing * deltaSeconds
    }

    fun draw(canvas: Canvas) {
        if (!isActive) return

        val hw = Constants.BIRD_WIDTH / 2f
        val hh = Constants.BIRD_HEIGHT / 2f

        // Glow ring
        glowRect.set(x - hw - 8f, y - hh - 8f, x + hw + 8f, y + hh + 8f)
        canvas.drawOval(glowRect, glowPaint)

        // Ghost body
        bodyRect.set(x - hw, y - hh, x + hw, y + hh)
        canvas.drawOval(bodyRect, bodyPaint)

        // Eye
        bodyPaint.alpha = 160
        canvas.drawCircle(x + hw * 0.35f, y - hh * 0.2f, 8f, bodyPaint)
        bodyPaint.alpha = Constants.GHOST_ALPHA

        // Opponent score label above ghost
        canvas.drawText("opp: $opponentScore", x, y - hh - 16f, scorePaint)
    }
}
