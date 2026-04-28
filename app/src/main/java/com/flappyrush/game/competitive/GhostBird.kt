package com.flappyrush.game.competitive

import android.graphics.*
import com.flappyrush.utils.Constants

// Ghost bird renders your opponent's bird semi-transparently — Phase 4
class GhostBird(screenWidth: Int, screenHeight: Int) {

    var x: Float = screenWidth * Constants.BIRD_START_X_RATIO
    var y: Float = screenHeight / 2f
    var isActive: Boolean = false

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        alpha = Constants.GHOST_ALPHA
    }
    private val bodyRect = RectF()

    // Opponent's position snapshots streamed from Firebase
    private val positionBuffer = ArrayDeque<Pair<Float, Float>>()

    fun receivePosition(opponentY: Float, timestamp: Long) {
        // TODO Phase 4: buffer opponent position updates from Firebase
        positionBuffer.addLast(Pair(opponentY, timestamp.toFloat()))
        if (positionBuffer.size > 60) positionBuffer.removeFirst()
    }

    fun update() {
        // TODO Phase 4: interpolate between buffered positions for smooth rendering
        positionBuffer.firstOrNull()?.let { y = it.first }
    }

    fun draw(canvas: Canvas) {
        if (!isActive) return

        paint.color = Color.argb(Constants.GHOST_ALPHA, 100, 180, 255)
        bodyRect.set(
            x - Constants.BIRD_WIDTH / 2f,
            y - Constants.BIRD_HEIGHT / 2f,
            x + Constants.BIRD_WIDTH / 2f,
            y + Constants.BIRD_HEIGHT / 2f
        )
        canvas.drawOval(bodyRect, paint)
    }
}
