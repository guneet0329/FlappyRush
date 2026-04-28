package com.flappyrush.game.screens.match

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.flappyrush.game.competitive.MatchManager

class MatchmakingActivity : AppCompatActivity() {

    private val matchManager = MatchManager()
    private val handler = Handler(Looper.getMainLooper())
    private var searchSeconds = 0
    private lateinit var statusText: TextView
    private lateinit var timerText: TextView
    private lateinit var cancelBtn: Button

    private val tickRunnable = object : Runnable {
        override fun run() {
            searchSeconds++
            timerText.text = "Searching... ${searchSeconds}s"
            if (searchSeconds >= 60) {
                onTimeout()
                return
            }
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUI()
        startSearch()
    }

    private fun buildUI() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(android.graphics.Color.parseColor("#1A1A2E"))
            setPadding(60, 100, 60, 100)
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 24, 0, 0) }

        val title = TextView(this).apply {
            text = "FINDING MATCH"
            textSize = 30f
            gravity = Gravity.CENTER
            setTextColor(android.graphics.Color.parseColor("#FFD700"))
            typeface = android.graphics.Typeface.create(
                android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }

        val spinner = ProgressBar(this)

        statusText = TextView(this).apply {
            text = "Looking for an opponent..."
            textSize = 18f
            gravity = Gravity.CENTER
            setTextColor(android.graphics.Color.WHITE)
        }

        timerText = TextView(this).apply {
            text = "Searching... 0s"
            textSize = 14f
            gravity = Gravity.CENTER
            setTextColor(android.graphics.Color.parseColor("#AAAAAA"))
        }

        cancelBtn = Button(this).apply {
            text = "CANCEL"
            setBackgroundColor(android.graphics.Color.parseColor("#37474F"))
            setTextColor(android.graphics.Color.WHITE)
            setOnClickListener {
                matchManager.cancelSearch()
                finish()
            }
        }

        root.addView(title, params)
        root.addView(spinner, params)
        root.addView(statusText, params)
        root.addView(timerText, params)
        root.addView(cancelBtn, params)
        setContentView(root)
    }

    private fun startSearch() {
        handler.post(tickRunnable)

        matchManager.onMatchFound = { matchId, seed ->
            handler.post {
                statusText.text = "Opponent found!"
                handler.postDelayed({
                    val intent = Intent(this, MultiplayerGameActivity::class.java).apply {
                        putExtra("matchId", matchId)
                        putExtra("seed", seed)
                        putExtra("opponentUid", matchManager.opponentUid)
                        putExtra("isPlayer1", matchManager.isPlayer1)
                    }
                    startActivity(intent)
                    finish()
                }, 800)
            }
        }

        matchManager.findMatch()
    }

    private fun onTimeout() {
        statusText.text = "No opponent found. Try again!"
        cancelBtn.text = "BACK"
        matchManager.cancelSearch()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(tickRunnable)
    }
}
