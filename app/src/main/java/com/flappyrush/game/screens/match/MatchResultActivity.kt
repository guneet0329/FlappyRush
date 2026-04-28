package com.flappyrush.game.screens.match

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.flappyrush.auth.AuthManager
import com.flappyrush.data.repository.PlayerRepository
import com.flappyrush.game.screens.MenuActivity

class MatchResultActivity : AppCompatActivity() {

    private val auth = AuthManager()
    private val playerRepo = PlayerRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val winnerId     = intent.getStringExtra("winnerId") ?: ""
        val myScore      = intent.getIntExtra("myScore", 0)
        val opponentScore = intent.getIntExtra("opponentScore", 0)
        val uid          = auth.uid ?: ""
        val iWon         = winnerId == uid
        val isDraw       = winnerId.isEmpty()

        // Update season points
        val pointsGained = when {
            iWon   -> 30
            isDraw -> 10
            else   -> 5
        }
        playerRepo.updateBestScore(uid, myScore)

        buildUI(iWon, isDraw, myScore, opponentScore, pointsGained)
    }

    private fun buildUI(iWon: Boolean, isDraw: Boolean, myScore: Int, oppScore: Int, points: Int) {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(android.graphics.Color.parseColor("#1A1A2E"))
            setPadding(60, 80, 60, 80)
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 24, 0, 0) }

        val resultColor = when {
            iWon   -> "#FFD700"
            isDraw -> "#AAAAAA"
            else   -> "#FF6B35"
        }
        val resultText = when {
            iWon   -> "🏆  VICTORY!"
            isDraw -> "🤝  DRAW"
            else   -> "💀  DEFEAT"
        }

        val resultLabel = TextView(this).apply {
            text = resultText
            textSize = 44f
            gravity = Gravity.CENTER
            setTextColor(android.graphics.Color.parseColor(resultColor))
            typeface = android.graphics.Typeface.create(
                android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }

        val scoreRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        fun scoreBox(label: String, score: Int, color: String): LinearLayout {
            return LinearLayout(this@MatchResultActivity).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(40, 20, 40, 20)
                addView(TextView(this@MatchResultActivity).apply {
                    text = score.toString()
                    textSize = 52f
                    gravity = Gravity.CENTER
                    setTextColor(android.graphics.Color.parseColor(color))
                    typeface = android.graphics.Typeface.create(
                        android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                })
                addView(TextView(this@MatchResultActivity).apply {
                    text = label
                    textSize = 14f
                    gravity = Gravity.CENTER
                    setTextColor(android.graphics.Color.parseColor("#AAAAAA"))
                })
            }
        }

        val vsText = TextView(this).apply {
            text = "VS"
            textSize = 24f
            setPadding(30, 0, 30, 0)
            setTextColor(android.graphics.Color.parseColor("#555555"))
        }

        scoreRow.addView(scoreBox("YOU", myScore, "#FFD700"))
        scoreRow.addView(vsText)
        scoreRow.addView(scoreBox("OPP", oppScore, "#00E5FF"))

        val pointsLabel = TextView(this).apply {
            text = "+$points season points"
            textSize = 18f
            gravity = Gravity.CENTER
            setTextColor(android.graphics.Color.parseColor("#4CAF50"))
        }

        val playAgainBtn = Button(this).apply {
            text = "PLAY AGAIN"
            setBackgroundColor(android.graphics.Color.parseColor("#FF6B35"))
            setTextColor(android.graphics.Color.WHITE)
            setOnClickListener {
                startActivity(Intent(this@MatchResultActivity, MatchmakingActivity::class.java))
                finish()
            }
        }

        val menuBtn = Button(this).apply {
            text = "MAIN MENU"
            setBackgroundColor(android.graphics.Color.parseColor("#37474F"))
            setTextColor(android.graphics.Color.WHITE)
            setOnClickListener {
                startActivity(Intent(this@MatchResultActivity, MenuActivity::class.java))
                finish()
            }
        }

        root.addView(resultLabel, params)
        root.addView(scoreRow, params)
        root.addView(pointsLabel, params)
        root.addView(playAgainBtn, params)
        root.addView(menuBtn, params)

        setContentView(root)
    }
}
