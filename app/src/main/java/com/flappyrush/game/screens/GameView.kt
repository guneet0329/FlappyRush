package com.flappyrush.game.screens

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.flappyrush.game.engine.GameEngine
import com.flappyrush.game.engine.GameLoop
import com.flappyrush.game.ui.HUD
import com.flappyrush.game.ui.ScoreOverlay

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private lateinit var engine: GameEngine
    private lateinit var gameLoop: GameLoop
    private lateinit var hud: HUD
    private lateinit var scoreOverlay: ScoreOverlay

    var onGameOver: ((score: Int) -> Unit)? = null

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        engine = GameEngine(context, width, height)
        hud = HUD(width)
        scoreOverlay = ScoreOverlay(context, width, height)

        engine.onGameOver = { score ->
            scoreOverlay.saveBestScore(score)
            onGameOver?.invoke(score)
        }

        gameLoop = GameLoop { deltaSeconds ->
            engine.update(deltaSeconds)
            drawFrame()
        }
        gameLoop.startLoop()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameLoop.stopLoop()
        engine.release()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!::engine.isInitialized) return false

        when (engine.state) {
            GameEngine.GameState.MENU -> {
                engine.startGame()
                return true
            }
            GameEngine.GameState.PLAYING -> {
                engine.input.onTouch(event)
                return true
            }
            GameEngine.GameState.DEAD -> {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    engine.startGame()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun drawFrame() {
        val canvas: Canvas = holder.lockCanvas() ?: return
        try {
            canvas.drawColor(Color.BLACK)
            engine.draw(canvas)

            when (engine.state) {
                GameEngine.GameState.MENU -> {
                    hud.drawTapToStart(canvas, height)
                }
                GameEngine.GameState.PLAYING -> {
                    hud.draw(canvas, engine.pipeManager.score)
                }
                GameEngine.GameState.DEAD -> {
                    hud.draw(canvas, engine.pipeManager.score)
                    scoreOverlay.draw(canvas, engine.pipeManager.score)
                }
            }
        } finally {
            holder.unlockCanvasAndPost(canvas)
        }
    }
}
