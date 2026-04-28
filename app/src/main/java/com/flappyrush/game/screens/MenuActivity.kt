package com.flappyrush.game.screens

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.flappyrush.auth.AuthManager
import com.flappyrush.data.repository.PlayerRepository
import com.flappyrush.game.screens.auth.LoginActivity
import com.flappyrush.game.screens.profile.ProfileActivity

class MenuActivity : AppCompatActivity() {

    private val auth = AuthManager()
    private val playerRepo = PlayerRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Redirect to login if not authenticated
        if (!auth.isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        buildUI()
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
        ).apply { setMargins(0, 20, 0, 0) }

        val title = TextView(this).apply {
            text = "FLAPPY RUSH"
            textSize = 42f
            gravity = Gravity.CENTER
            setTextColor(android.graphics.Color.parseColor("#FFD700"))
            typeface = android.graphics.Typeface.create(
                android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }

        val subtitle = TextView(this).apply {
            text = "Competitive Edition"
            textSize = 18f
            gravity = Gravity.CENTER
            setTextColor(android.graphics.Color.parseColor("#FF6B35"))
        }

        // Load username
        val welcomeText = TextView(this).apply {
            text = "Welcome back!"
            textSize = 16f
            gravity = Gravity.CENTER
            setTextColor(android.graphics.Color.parseColor("#AAAAAA"))
        }
        auth.uid?.let { uid ->
            playerRepo.listenToPlayer(uid) { player ->
                runOnUiThread {
                    welcomeText.text = "Welcome, ${player?.username ?: "Player"}!"
                }
            }
        }

        val playBtn = Button(this).apply {
            text = "▶  PLAY"
            textSize = 22f
            setBackgroundColor(android.graphics.Color.parseColor("#FF6B35"))
            setTextColor(android.graphics.Color.WHITE)
            setOnClickListener {
                startActivity(Intent(this@MenuActivity, GameActivity::class.java))
            }
        }

        val profileBtn = Button(this).apply {
            text = "👤  PROFILE & LEADERBOARD"
            setBackgroundColor(android.graphics.Color.parseColor("#16213E"))
            setTextColor(android.graphics.Color.WHITE)
            setOnClickListener {
                startActivity(Intent(this@MenuActivity, ProfileActivity::class.java))
            }
        }

        val logoutBtn = Button(this).apply {
            text = "Sign out"
            setBackgroundColor(android.graphics.Color.parseColor("#37474F"))
            setTextColor(android.graphics.Color.parseColor("#AAAAAA"))
            setOnClickListener {
                auth.signOut()
                startActivity(Intent(this@MenuActivity, LoginActivity::class.java))
                finish()
            }
        }

        root.addView(title, params)
        root.addView(subtitle, params)
        root.addView(welcomeText, params)
        root.addView(playBtn, params)
        root.addView(profileBtn, params)
        root.addView(logoutBtn, params)

        setContentView(root)
    }
}
