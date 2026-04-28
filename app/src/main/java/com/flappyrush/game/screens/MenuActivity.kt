package com.flappyrush.game.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.flappyrush.utils.Constants

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // For Phase 1 — launch directly into game
        // Phase 2 will add a proper menu layout
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
        finish()
    }
}
