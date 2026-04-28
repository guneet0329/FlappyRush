package com.flappyrush.game.objects

import android.graphics.Canvas
import com.flappyrush.game.theme.PipeTheme
import com.flappyrush.utils.Constants
import kotlin.random.Random

class PipeManager(private val screenWidth: Int, private val screenHeight: Int) {

    private val pipes = mutableListOf<Pipe>()
    var currentSpeed: Float = Constants.PIPE_SPEED_INITIAL
    var score: Int = 0
    var pipeTheme: PipeTheme = PipeTheme.CLASSIC
    var onScorePoint: (() -> Unit)? = null

    private val minGapY = screenHeight * 0.25f
    private val maxGapY = screenHeight * 0.75f

    init { spawnInitialPipes() }

    private fun spawnInitialPipes() {
        var nextX = screenWidth + 200f
        repeat(3) { spawnPipeAt(nextX); nextX += Constants.PIPE_SPACING }
    }

    private fun spawnPipeAt(x: Float) {
        val gapY = Random.nextFloat() * (maxGapY - minGapY) + minGapY
        pipes.add(Pipe(x, gapY, screenHeight, pipeTheme))
    }

    fun update(deltaSeconds: Float) {
        pipes.forEach { it.update(currentSpeed, deltaSeconds) }

        pipes.filter { !it.passed && it.x + Constants.PIPE_WIDTH < screenWidth * Constants.BIRD_START_X_RATIO }
            .forEach {
                it.passed = true
                score++
                currentSpeed = (Constants.PIPE_SPEED_INITIAL + score * Constants.PIPE_SPEED_INCREMENT)
                    .coerceAtMost(Constants.PIPE_SPEED_MAX)
                onScorePoint?.invoke()
            }

        pipes.removeAll { it.isOffScreen() }

        val lastPipe = pipes.maxByOrNull { it.x }
        if (lastPipe == null || lastPipe.x < screenWidth + Constants.PIPE_SPACING - Constants.PIPE_WIDTH) {
            spawnPipeAt((lastPipe?.x ?: screenWidth.toFloat()) + Constants.PIPE_SPACING)
        }
    }

    fun checkCollision(bird: Bird): Boolean {
        return pipes.any { pipe ->
            val hb = bird.hitbox
            hb.intersect(pipe.topHitbox) || hb.intersect(pipe.bottomHitbox)
        }
    }

    fun draw(canvas: Canvas) = pipes.forEach { it.draw(canvas) }

    fun reset() {
        pipes.clear()
        currentSpeed = Constants.PIPE_SPEED_INITIAL
        score = 0
        spawnInitialPipes()
    }
}
