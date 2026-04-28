package com.flappyrush.game.objects

import android.graphics.*
import com.flappyrush.utils.Constants

class Bird(screenWidth: Int, screenHeight: Int) {

    var x: Float = screenWidth * Constants.BIRD_START_X_RATIO
    var y: Float = screenHeight / 2f
    var velocityY: Float = 0f
    var isAlive: Boolean = true

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bodyRect = RectF()
    private val wingRect = RectF()

    // Colors — Phase 2 will replace with sprites
    private val bodyColor = Color.parseColor("#FFD700")  // gold
    private val wingColor = Color.parseColor("#FFA500")  // orange
    private val eyeColor  = Color.WHITE
    private val pupilColor = Color.BLACK
    private val beakColor = Color.parseColor("#FF6B35")

    val hitbox: RectF get() = RectF(
        x - Constants.BIRD_WIDTH / 2f + 8f,
        y - Constants.BIRD_HEIGHT / 2f + 6f,
        x + Constants.BIRD_WIDTH / 2f - 8f,
        y + Constants.BIRD_HEIGHT / 2f - 6f
    )

    fun flap() {
        if (isAlive) velocityY = Constants.FLAP_VELOCITY
    }

    fun update(deltaSeconds: Float) {
        if (!isAlive) return
        velocityY += Constants.GRAVITY * deltaSeconds
        velocityY = velocityY.coerceAtMost(Constants.MAX_FALL_VELOCITY)
        y += velocityY * deltaSeconds
    }

    fun draw(canvas: Canvas) {
        val rotation = when {
            velocityY < -200 -> Constants.BIRD_ROTATION_UP
            velocityY > 300  -> Constants.BIRD_ROTATION_DOWN
            else -> velocityY / 300f * Constants.BIRD_ROTATION_DOWN
        }

        canvas.save()
        canvas.rotate(rotation, x, y)

        val hw = Constants.BIRD_WIDTH / 2f
        val hh = Constants.BIRD_HEIGHT / 2f

        // Body
        paint.color = bodyColor
        bodyRect.set(x - hw, y - hh, x + hw, y + hh)
        canvas.drawOval(bodyRect, paint)

        // Wing (flapping offset based on velocity)
        val wingOffset = if (velocityY < 0) -8f else 4f
        paint.color = wingColor
        wingRect.set(x - hw * 0.5f, y - hh * 0.3f + wingOffset, x + hw * 0.3f, y + hh * 0.2f + wingOffset)
        canvas.drawOval(wingRect, paint)

        // Eye white
        paint.color = eyeColor
        canvas.drawCircle(x + hw * 0.35f, y - hh * 0.2f, 10f, paint)

        // Pupil
        paint.color = pupilColor
        canvas.drawCircle(x + hw * 0.45f, y - hh * 0.2f, 5f, paint)

        // Beak
        paint.color = beakColor
        val beakPath = Path().apply {
            moveTo(x + hw * 0.7f, y + 2f)
            lineTo(x + hw * 1.1f, y - 4f)
            lineTo(x + hw * 1.1f, y + 8f)
            close()
        }
        canvas.drawPath(beakPath, paint)

        canvas.restore()
    }

    fun reset(screenWidth: Int, screenHeight: Int) {
        x = screenWidth * Constants.BIRD_START_X_RATIO
        y = screenHeight / 2f
        velocityY = 0f
        isAlive = true
    }
}
