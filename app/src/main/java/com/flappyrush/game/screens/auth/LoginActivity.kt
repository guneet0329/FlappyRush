package com.flappyrush.game.screens.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.flappyrush.auth.AuthManager
import com.flappyrush.game.screens.MenuActivity

class LoginActivity : AppCompatActivity() {

    private val auth = AuthManager()

    // Views — wired up programmatically (no XML layout needed for now)
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var usernameField: EditText
    private lateinit var loginBtn: Button
    private lateinit var signUpBtn: Button
    private lateinit var guestBtn: Button
    private lateinit var statusText: TextView
    private lateinit var loadingSpinner: ProgressBar
    private var isSignUpMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Skip if already logged in
        if (auth.isLoggedIn) {
            goToMenu()
            return
        }

        buildUI()
    }

    private fun buildUI() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(64, 120, 64, 64)
            setBackgroundColor(android.graphics.Color.parseColor("#1A1A2E"))
        }

        val titleView = TextView(this).apply {
            text = "FLAPPY RUSH"
            textSize = 36f
            setTextColor(android.graphics.Color.parseColor("#FFD700"))
            gravity = android.view.Gravity.CENTER
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }

        val subtitleView = TextView(this).apply {
            text = "Competitive Edition"
            textSize = 16f
            setTextColor(android.graphics.Color.parseColor("#FF6B35"))
            gravity = android.view.Gravity.CENTER
        }

        usernameField = EditText(this).apply {
            hint = "Username"
            visibility = View.GONE
            setTextColor(android.graphics.Color.WHITE)
            setHintTextColor(android.graphics.Color.GRAY)
        }

        emailField = EditText(this).apply {
            hint = "Email"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            setTextColor(android.graphics.Color.WHITE)
            setHintTextColor(android.graphics.Color.GRAY)
        }

        passwordField = EditText(this).apply {
            hint = "Password"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            setTextColor(android.graphics.Color.WHITE)
            setHintTextColor(android.graphics.Color.GRAY)
        }

        loginBtn = Button(this).apply {
            text = "LOG IN"
            setBackgroundColor(android.graphics.Color.parseColor("#FF6B35"))
            setTextColor(android.graphics.Color.WHITE)
            setOnClickListener { handleLogin() }
        }

        signUpBtn = Button(this).apply {
            text = "CREATE ACCOUNT"
            setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"))
            setTextColor(android.graphics.Color.WHITE)
            setOnClickListener { toggleSignUp() }
        }

        guestBtn = Button(this).apply {
            text = "PLAY AS GUEST"
            setBackgroundColor(android.graphics.Color.parseColor("#455A64"))
            setTextColor(android.graphics.Color.WHITE)
            setOnClickListener { handleGuest() }
        }

        statusText = TextView(this).apply {
            setTextColor(android.graphics.Color.RED)
            textSize = 14f
            gravity = android.view.Gravity.CENTER
        }

        loadingSpinner = ProgressBar(this).apply {
            visibility = View.GONE
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 20, 0, 0) }

        layout.addView(titleView, params)
        layout.addView(subtitleView, params)
        layout.addView(usernameField, params)
        layout.addView(emailField, params)
        layout.addView(passwordField, params)
        layout.addView(loginBtn, params)
        layout.addView(signUpBtn, params)
        layout.addView(guestBtn, params)
        layout.addView(statusText, params)
        layout.addView(loadingSpinner, params)

        setContentView(layout)
    }

    private fun toggleSignUp() {
        isSignUpMode = !isSignUpMode
        usernameField.visibility = if (isSignUpMode) View.VISIBLE else View.GONE
        loginBtn.text = if (isSignUpMode) "CREATE" else "LOG IN"
        signUpBtn.text = if (isSignUpMode) "BACK TO LOGIN" else "CREATE ACCOUNT"
    }

    private fun handleLogin() {
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString()
        if (email.isEmpty() || password.isEmpty()) { statusText.text = "Fill in all fields"; return }

        setLoading(true)
        if (isSignUpMode) {
            val username = usernameField.text.toString().trim()
            if (username.isEmpty()) { statusText.text = "Enter a username"; setLoading(false); return }
            auth.signUp(email, password, username) { ok, err ->
                setLoading(false)
                if (ok) goToMenu() else statusText.text = err ?: "Sign up failed"
            }
        } else {
            auth.signIn(email, password) { ok, err ->
                setLoading(false)
                if (ok) goToMenu() else statusText.text = err ?: "Login failed"
            }
        }
    }

    private fun handleGuest() {
        setLoading(true)
        auth.signInAnonymously { ok ->
            setLoading(false)
            if (ok) goToMenu() else statusText.text = "Guest sign in failed"
        }
    }

    private fun setLoading(loading: Boolean) {
        loadingSpinner.visibility = if (loading) View.VISIBLE else View.GONE
        loginBtn.isEnabled = !loading
        signUpBtn.isEnabled = !loading
        guestBtn.isEnabled = !loading
    }

    private fun goToMenu() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }
}
