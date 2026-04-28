package com.flappyrush.game.screens.match

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.flappyrush.game.competitive.GhostBird
import com.flappyrush.game.competitive.MatchManager
import com.flappyrush.game.screens.match.MultiplayerGameView

class MultiplayerGameActivity : AppCompatActivity() {

    private lateinit var gameView: MultiplayerGameView
    private val matchManager = MatchManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.decorView.systemUiVisibility = (
            android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
            android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )

        val matchId     = intent.getStringExtra("matchId") ?: ""
        val seed        = intent.getLongExtra("seed", 0L)
        val opponentUid = intent.getStringExtra("opponentUid") ?: ""
        val isPlayer1   = intent.getBooleanExtra("isPlayer1", true)

        matchManager.opponentUid = opponentUid
        matchManager.isPlayer1   = isPlayer1

        gameView = MultiplayerGameView(this, matchId, seed, matchManager)
        setContentView(gameView)

        // Listen for opponent position updates
        matchManager.listenToOpponent(matchId)
        matchManager.onOpponentPosition = { y, score ->
            gameView.updateOpponent(y, score)
        }

        // Match finished
        matchManager.onMatchFinished = { winnerId ->
            runOnUiThread {
                val intent = Intent(this, MatchResultActivity::class.java).apply {
                    putExtra("winnerId", winnerId)
                    putExtra("matchId", matchId)
                    putExtra("myScore", gameView.myScore)
                    putExtra("opponentScore", gameView.opponentScore)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val matchId = intent.getStringExtra("matchId") ?: ""
        matchManager.cleanup(matchId)
    }
}
