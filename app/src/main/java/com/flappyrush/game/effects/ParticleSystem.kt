package com.flappyrush.game.effects

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class ParticleSystem {

    private val particles = mutableListOf<Particle>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    data class Particle(
        var x: Float,
        var y: Float,
        var vx: Float,
        var vy: Float,
        var radius: Float,
        var alpha: Float,       // 0-255
        var color: Int,
        var life: Float,        // 0-1, counts down
        var decay: Float        // how fast it dies
    )

    fun emitFlap(x: Float, y: Float, trailColor: Int) {
        repeat(5) {
            val angle = Random.nextFloat() * Math.PI.toFloat() + Math.PI.toFloat() // left hemisphere
            val speed = Random.nextFloat() * 180f + 60f
            particles.add(Particle(
                x = x, y = y,
                vx = cos(angle) * speed,
                vy = sin(angle) * speed - 80f,
                radius = Random.nextFloat() * 6f + 3f,
                alpha = 200f,
                color = trailColor,
                life = 1f,
                decay = Random.nextFloat() * 0.04f + 0.03f
            ))
        }
    }

    fun emitScore(x: Float, y: Float) {
        repeat(12) {
            val angle = Random.nextFloat() * 2 * Math.PI.toFloat()
            val speed = Random.nextFloat() * 300f + 100f
            val colors = listOf(0xFFFFD700.toInt(), 0xFFFF6B35.toInt(), 0xFF00E5FF.toInt(), 0xFFFFFFFF.toInt())
            particles.add(Particle(
                x = x, y = y,
                vx = cos(angle) * speed,
                vy = sin(angle) * speed,
                radius = Random.nextFloat() * 8f + 4f,
                alpha = 255f,
                color = colors.random(),
                life = 1f,
                decay = Random.nextFloat() * 0.025f + 0.015f
            ))
        }
    }

    fun emitDeath(x: Float, y: Float) {
        repeat(20) {
            val angle = Random.nextFloat() * 2 * Math.PI.toFloat()
            val speed = Random.nextFloat() * 400f + 150f
            particles.add(Particle(
                x = x, y = y,
                vx = cos(angle) * speed,
                vy = sin(angle) * speed,
                radius = Random.nextFloat() * 10f + 5f,
                alpha = 255f,
                color = 0xFFFF1744.toInt(),
                life = 1f,
                decay = Random.nextFloat() * 0.02f + 0.01f
            ))
        }
    }

    fun update(deltaSeconds: Float) {
        val gravity = 400f
        particles.forEach { p ->
            p.x += p.vx * deltaSeconds
            p.y += p.vy * deltaSeconds
            p.vy += gravity * deltaSeconds
            p.life -= p.decay
            p.alpha = (p.life * 255f).coerceIn(0f, 255f)
        }
        particles.removeAll { it.life <= 0f }
    }

    fun draw(canvas: Canvas) {
        particles.forEach { p ->
            paint.color = p.color
            paint.alpha = p.alpha.toInt()
            canvas.drawCircle(p.x, p.y, p.radius * p.life, paint)
        }
    }

    fun clear() = particles.clear()
}
