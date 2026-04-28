package com.flappyrush.game.objects

import android.graphics.Canvas
import com.flappyrush.utils.Constants
import kotlin.random.Random

class PipeManager(private val screenWidth: Int, private val screenHeight: Int) {

    private val pipes = mutableListOf<Pipe>()
    var currentSpeed: Float = Constants.PIPE_SPEED_INITIAL
    var score: Int = 0
    var onScorePoint: (() -> Unit)? = null

    // Vertical padding — pipes won't spawn too close to top/bottom
    private val minGapY = screenHeight * 0.25f
    private val maxGapY = screenHeight * 0.75f

    init {
        spawnInitialPipes()
    }

    private fun spawnInitialPipes() {
        var nextX = screenWidth + 200f
        repeat(3) {
            spawnPipeAt(nextX)
            nextX += Constants.PIPE_SPACING
        }
    }

    private fun spawnPipeAt(x: Float) {
        val gapY = Random.nextFloat() * (maxGapY - minGapY) + minGapY
        pipes.add(Pipe(x, gapY, screenHeight))
    }

    fun update(deltaSeconds: Float) {
        pipes.forEach { it.update(currentSpeed, deltaSeconds) }

        // Score: pipe center passes bird's x position
        pipes.filter { !it.passed && it.x + Constants.PIPE_WIDTH < screenWidth * Constants.BIRD_START_X_RATIO }.forEach {
            it.passed = true
            score++
            currentSpeed = (Constants.PIPE_SPEED_INITIAL + score * Constants.PIPE_SPEED_INCREMENT)
                .coerceAtMost(Constants.PIPE_SPEED_MAX)
            onScorePoint?.invoke()
        }

        // Remove offscreen pipes
        pipes.removeAll { it.isOffScreen() }

        // Spawn new pipe when last pipe has scrolled enough
        val lastPipe = pipes.maxByOrNull { it.x }
        if (lastPipe == null || lastPipe.x < screenWidth + Constants.PIPE_SPACING - Constants.PIPE_WIDTH) {
            spawnPipeAt((lastPipe?.x ?: screenWidth.toFloat()) + Constants.PIPE_SPACING)
        }
    }

    fun checkCollision(bird: Bird): Boolean {
        return pipes.any { pipe ->
            bird.hitbox.intersect(pipe.topHitbox) || bird.hitbox.intersect(pipe.bottomHitbox)
        }
    }

    fun draw(canvas: Canvas) {
        pipes.forEach { it.draw(canvas) }
    }

    fun reset() {
        pipes.clear()
        currentSpeed = Constants.PIPE_SPEED_INITIAL
        score = 0
        spawnInitialPipes()
    }
}
