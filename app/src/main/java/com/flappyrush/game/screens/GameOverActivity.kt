package com.flappyrush.game.screens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// Stub for Phase 3 — will show detailed stats, leaderboard preview, share button
class GameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val score = intent.getIntExtra("score", 0)
        // TODO Phase 3: show score breakdown, leaderboard rank, replay button
        startActivity(Intent(this, GameActivity::class.java))
        finish()
    }
}
