package com.flappyrush.game.engine

import android.content.Context
import android.graphics.Canvas
import com.flappyrush.game.objects.*
import com.flappyrush.utils.SoundManager

class GameEngine(context: Context, private val screenWidth: Int, private val screenHeight: Int) {

    val bird = Bird(screenWidth, screenHeight)
    val pipeManager = PipeManager(screenWidth, screenHeight)
    val background = Background(screenWidth, screenHeight)
    val physics = PhysicsEngine()
    val input = InputHandler(bird)
    val sound = SoundManager(context)

    var state: GameState = GameState.MENU
    var onGameOver: ((score: Int) -> Unit)? = null
    var onScore: (() -> Unit)? = null

    enum class GameState { MENU, PLAYING, DEAD }

    init {
        pipeManager.onScorePoint = {
            sound.play(SoundManager.SoundEvent.SCORE)
            onScore?.invoke()
        }
    }

    fun startGame() {
        bird.reset(screenWidth, screenHeight)
        pipeManager.reset()
        state = GameState.PLAYING
    }

    fun update(deltaSeconds: Float) {
        if (state != GameState.PLAYING) return

        val collision = physics.update(bird, pipeManager, background, deltaSeconds)

        if (collision != PhysicsEngine.CollisionResult.NONE) {
            bird.isAlive = false
            state = GameState.DEAD
            sound.play(SoundManager.SoundEvent.HIT)
            onGameOver?.invoke(pipeManager.score)
        }
    }

    fun draw(canvas: Canvas) {
        background.draw(canvas)
        pipeManager.draw(canvas)
        bird.draw(canvas)
    }

    fun release() {
        sound.release()
    }
}
