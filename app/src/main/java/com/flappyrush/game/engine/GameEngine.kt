package com.flappyrush.game.engine

import android.content.Context
import android.graphics.Canvas
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.os.Build
import com.flappyrush.game.effects.ParticleSystem
import com.flappyrush.game.effects.ScreenShake
import com.flappyrush.game.objects.*
import com.flappyrush.game.theme.BirdSkin
import com.flappyrush.game.theme.PipeTheme
import com.flappyrush.game.ui.HUD
import com.flappyrush.utils.SoundManager

class GameEngine(private val context: Context, val screenWidth: Int, val screenHeight: Int) {

    val bird = Bird(screenWidth, screenHeight)
    val pipeManager = PipeManager(screenWidth, screenHeight)
    val background = Background(screenWidth, screenHeight)
    val physics = PhysicsEngine()
    val input = InputHandler(bird)
    val sound = SoundManager(context)
    val particles = ParticleSystem()
    val shake = ScreenShake()

    var state: GameState = GameState.MENU
    var onGameOver: ((score: Int) -> Unit)? = null
    var onScore: (() -> Unit)? = null

    // Active skin/theme (Phase 5 will load from player prefs)
    var activeSkin: BirdSkin = BirdSkin.GOLDEN
    var activeTheme: PipeTheme = PipeTheme.CLASSIC

    enum class GameState { MENU, PLAYING, DEAD }

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    init {
        pipeManager.onScorePoint = {
            sound.play(SoundManager.SoundEvent.SCORE)
            particles.emitScore(
                screenWidth * 0.5f,
                screenHeight * 0.15f
            )
            onScore?.invoke()
        }
    }

    fun startGame() {
        bird.skin = activeSkin
        pipeManager.pipeTheme = activeTheme
        bird.reset(screenWidth, screenHeight)
        pipeManager.reset()
        particles.clear()
        state = GameState.PLAYING
    }

    fun update(deltaSeconds: Float) {
        shake.update(deltaSeconds)
        particles.update(deltaSeconds)

        if (state != GameState.PLAYING) return

        background.update(pipeManager.currentSpeed, deltaSeconds)

        // Emit trail particles on flap
        if (bird.velocityY < -400f) {
            particles.emitFlap(bird.x - 20f, bird.y, bird.skin.trailColor)
        }

        val collision = physics.update(bird, pipeManager, background, deltaSeconds)

        if (collision != PhysicsEngine.CollisionResult.NONE) {
            bird.isAlive = false
            state = GameState.DEAD
            sound.play(SoundManager.SoundEvent.HIT)
            shake.shake(22f, 0.35f)
            particles.emitDeath(bird.x, bird.y)
            vibrate(VibrationPattern.DEATH)
            onGameOver?.invoke(pipeManager.score)
        }
    }

    fun onTap() {
        if (state == GameState.PLAYING) {
            sound.play(SoundManager.SoundEvent.FLAP)
            vibrate(VibrationPattern.FLAP)
        }
    }

    fun draw(canvas: Canvas) {
        canvas.save()
        shake.apply(canvas)
        background.draw(canvas)
        pipeManager.draw(canvas)
        bird.draw(canvas)
        particles.draw(canvas)
        canvas.restore()
    }

    private enum class VibrationPattern { FLAP, DEATH }

    private fun vibrate(pattern: VibrationPattern) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = when (pattern) {
                VibrationPattern.FLAP  -> VibrationEffect.createOneShot(18, 40)
                VibrationPattern.DEATH -> VibrationEffect.createWaveform(
                    longArrayOf(0, 60, 40, 80), intArrayOf(0, 180, 0, 255), -1
                )
            }
            vibrator?.vibrate(effect)
        }
    }

    fun release() = sound.release()
}
