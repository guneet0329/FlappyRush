package com.flappyrush.game.screens.match

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.flappyrush.game.competitive.GhostBird
import com.flappyrush.game.competitive.MatchManager
import com.flappyrush.game.engine.GameEngine
import com.flappyrush.game.engine.GameLoop
import com.flappyrush.game.ui.HUD
import com.flappyrush.game.ui.ScoreOverlay

class MultiplayerGameView(
    context: Context,
    private val matchId: String,
    private val seed: Long,
    private val matchManager: MatchManager
) : SurfaceView(context), SurfaceHolder.Callback {

    private lateinit var engine: GameEngine
    private lateinit var gameLoop: GameLoop
    private lateinit var hud: HUD
    private lateinit var scoreOverlay: ScoreOverlay
    private lateinit var ghostBird: GhostBird

    var myScore = 0
    var opponentScore = 0
    private var isDead = false

    private val vsScorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 32f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        setShadowLayer(4f, 2f, 2f, Color.argb(120, 0, 0, 0))
    }

    init { holder.addCallback(this); isFocusable = true }

    override fun surfaceCreated(holder: SurfaceHolder) {
        engine = GameEngine(context, width, height)
        hud    = HUD(width, height)
        scoreOverlay = ScoreOverlay(context, width, height)
        ghostBird = GhostBird(width, height).apply { isActive = true }

        engine.onScore = {
            myScore = engine.pipeManager.score
            hud.onScore(myScore)
            matchManager.reportScore(matchId, myScore, engine.bird.y)
        }

        engine.onGameOver = { score ->
            myScore = score
            isDead = true
            matchManager.reportDeath(matchId, score)
        }

        engine.startGame()

        gameLoop = GameLoop { deltaSeconds ->
            if (!isDead) {
                engine.update(deltaSeconds)
                matchManager.reportScore(matchId, myScore, engine.bird.y)
            }
            ghostBird.update(deltaSeconds)
            hud.update(deltaSeconds)
            drawFrame()
        }
        gameLoop.startLoop()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameLoop.stopLoop()
        engine.release()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    fun updateOpponent(y: Float, score: Int) {
        ghostBird.receivePosition(y, score)
        opponentScore = score
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !isDead) {
            engine.input.onTouch(event)
            engine.onTap()
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun drawFrame() {
        val canvas: Canvas = holder.lockCanvas() ?: return
        try {
            canvas.drawColor(Color.BLACK)
            engine.draw(canvas)
            ghostBird.draw(canvas)

            // VS score bar at top
            drawVsBar(canvas)

            if (!isDead) hud.draw(canvas, myScore)
        } finally {
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun drawVsBar(canvas: Canvas) {
        val cx = width / 2f
        // Semi-transparent bar
        val barPaint = Paint().apply { color = Color.argb(120, 0, 0, 20) }
        canvas.drawRect(0f, 0f, width.toFloat(), 56f, barPaint)

        // You vs Opponent
        vsScorePaint.color = Color.parseColor("#FFD700")
        canvas.drawText("YOU: $myScore", cx * 0.5f, 38f, vsScorePaint)

        vsScorePaint.color = Color.WHITE
        canvas.drawText("VS", cx, 38f, vsScorePaint)

        vsScorePaint.color = Color.parseColor("#00E5FF")
        canvas.drawText("OPP: $opponentScore", cx * 1.5f, 38f, vsScorePaint)
    }
}
