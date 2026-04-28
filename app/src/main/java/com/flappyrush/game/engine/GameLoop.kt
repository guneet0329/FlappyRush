package com.flappyrush.game.engine

import com.flappyrush.utils.Constants

class GameLoop(private val onTick: (deltaSeconds: Float) -> Unit) : Thread() {

    @Volatile var running = false
    private var lastTime = 0L

    override fun run() {
        lastTime = System.nanoTime()
        while (running) {
            val now = System.nanoTime()
            val deltaNanos = now - lastTime
            lastTime = now

            val deltaSeconds = (deltaNanos / 1_000_000_000f).coerceAtMost(0.05f) // cap at 50ms

            onTick(deltaSeconds)

            // Sleep to target FPS
            val elapsed = (System.nanoTime() - now) / 1_000_000
            val sleepMs = Constants.GAME_TICK_MS - elapsed
            if (sleepMs > 0) sleep(sleepMs)
        }
    }

    fun startLoop() {
        running = true
        start()
    }

    fun stopLoop() {
        running = false
        try { join(500) } catch (e: InterruptedException) { interrupt() }
    }
}
