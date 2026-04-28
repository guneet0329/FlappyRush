package com.flappyrush.game.objects

import android.graphics.*
import com.flappyrush.game.theme.BirdSkin
import com.flappyrush.utils.Constants

class Bird(screenWidth: Int, screenHeight: Int) {

    var x: Float = screenWidth * Constants.BIRD_START_X_RATIO
    var y: Float = screenHeight / 2f
    var velocityY: Float = 0f
    var isAlive: Boolean = true
    var skin: BirdSkin = BirdSkin.GOLDEN

    // Wing animation
    private var wingAngle = 0f
    private var wingDirection = 1f
    private var flapBoost = 0f       // extra wing drop on tap

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bodyRect = RectF()
    private val wingRect = RectF()
    private val shadowRect = RectF()
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(40, 0, 0, 0)
    }

    val hitbox: RectF get() = RectF(
        x - Constants.BIRD_WIDTH / 2f + 10f,
        y - Constants.BIRD_HEIGHT / 2f + 8f,
        x + Constants.BIRD_WIDTH / 2f - 10f,
        y + Constants.BIRD_HEIGHT / 2f - 8f
    )

    fun flap() {
        if (isAlive) {
            velocityY = Constants.FLAP_VELOCITY
            flapBoost = 1f
        }
    }

    fun update(deltaSeconds: Float) {
        if (!isAlive) return
        velocityY += Constants.GRAVITY * deltaSeconds
        velocityY = velocityY.coerceAtMost(Constants.MAX_FALL_VELOCITY)
        y += velocityY * deltaSeconds

        // Wing flap animation
        wingAngle += wingDirection * 300f * deltaSeconds
        if (wingAngle > 20f) wingDirection = -1f
        if (wingAngle < -20f) wingDirection = 1f

        flapBoost = (flapBoost - deltaSeconds * 5f).coerceAtLeast(0f)
    }

    fun draw(canvas: Canvas) {
        val rotation = when {
            velocityY < -200 -> Constants.BIRD_ROTATION_UP
            velocityY > 300  -> Constants.BIRD_ROTATION_DOWN
            else -> velocityY / 300f * Constants.BIRD_ROTATION_DOWN
        }.coerceIn(Constants.BIRD_ROTATION_UP, Constants.BIRD_ROTATION_DOWN)

        canvas.save()
        canvas.rotate(rotation, x, y)

        val hw = Constants.BIRD_WIDTH / 2f
        val hh = Constants.BIRD_HEIGHT / 2f

        // Drop shadow
        shadowRect.set(x - hw * 0.9f, y + hh * 0.7f, x + hw * 0.9f, y + hh + 8f)
        canvas.drawOval(shadowRect, shadowPaint)

        // Body glow (outer soft ring)
        paint.color = skin.bodyColor
        paint.alpha = 60
        bodyRect.set(x - hw - 6f, y - hh - 6f, x + hw + 6f, y + hh + 6f)
        canvas.drawOval(bodyRect, paint)
        paint.alpha = 255

        // Body
        paint.color = skin.bodyColor
        bodyRect.set(x - hw, y - hh, x + hw, y + hh)
        canvas.drawOval(bodyRect, paint)

        // Wing (animated)
        val wingY = wingAngle + flapBoost * 12f
        paint.color = skin.wingColor
        wingRect.set(x - hw * 0.4f, y - hh * 0.2f + wingY, x + hw * 0.4f, y + hh * 0.35f + wingY)
        canvas.drawOval(wingRect, paint)

        // Body highlight
        paint.color = Color.argb(80, 255, 255, 255)
        canvas.drawOval(x - hw * 0.55f, y - hh * 0.65f, x + hw * 0.1f, y + hh * 0.1f, paint)

        // Eye white
        paint.color = Color.WHITE
        paint.alpha = 255
        canvas.drawCircle(x + hw * 0.32f, y - hh * 0.18f, 11f, paint)

        // Pupil (shifts on velocity for expressiveness)
        paint.color = Color.BLACK
        val eyeShiftX = (velocityY / Constants.MAX_FALL_VELOCITY * 3f).coerceIn(-3f, 3f)
        val eyeShiftY = (velocityY / Constants.MAX_FALL_VELOCITY * 2f).coerceIn(-2f, 2f)
        canvas.drawCircle(x + hw * 0.42f + eyeShiftX, y - hh * 0.18f + eyeShiftY, 5f, paint)

        // Eye shine
        paint.color = Color.WHITE
        canvas.drawCircle(x + hw * 0.38f, y - hh * 0.28f, 3f, paint)

        // Beak
        paint.color = skin.beakColor
        paint.alpha = 255
        val beakPath = Path().apply {
            moveTo(x + hw * 0.65f, y + 2f)
            lineTo(x + hw * 1.15f, y - 5f)
            lineTo(x + hw * 1.15f, y + 9f)
            close()
        }
        canvas.drawPath(beakPath, paint)

        // Beak divider line
        paint.color = Color.argb(80, 0, 0, 0)
        paint.strokeWidth = 1.5f
        paint.style = Paint.Style.STROKE
        canvas.drawLine(x + hw * 0.65f, y + 2f, x + hw * 1.15f, y + 2f, paint)
        paint.style = Paint.Style.FILL

        canvas.restore()
    }

    private fun Paint.drawOval(x1: Float, y1: Float, x2: Float, y2: Float, p: Paint) {}

    fun reset(screenWidth: Int, screenHeight: Int) {
        x = screenWidth * Constants.BIRD_START_X_RATIO
        y = screenHeight / 2f
        velocityY = 0f
        isAlive = true
        wingAngle = 0f
        flapBoost = 0f
    }
}
