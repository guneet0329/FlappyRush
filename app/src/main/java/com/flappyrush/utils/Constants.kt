package com.flappyrush.utils

object Constants {
    // Physics
    const val GRAVITY = 1800f
    const val FLAP_VELOCITY = -620f
    const val MAX_FALL_VELOCITY = 900f

    // Pipes
    const val PIPE_WIDTH = 120f
    const val PIPE_GAP = 320f
    const val PIPE_SPEED_INITIAL = 300f
    const val PIPE_SPEED_MAX = 520f
    const val PIPE_SPEED_INCREMENT = 5f
    const val PIPE_SPACING = 520f

    // Bird
    const val BIRD_WIDTH = 68f
    const val BIRD_HEIGHT = 52f
    const val BIRD_START_X_RATIO = 0.25f
    const val BIRD_ROTATION_UP = -25f
    const val BIRD_ROTATION_DOWN = 45f

    // Game loop
    const val TARGET_FPS = 60
    const val GAME_TICK_MS = (1000 / TARGET_FPS).toLong()

    // Scoring
    const val SCORE_PER_PIPE = 1
    const val BEST_SCORE_KEY = "best_score"

    // Ghost bird (Phase 4)
    const val GHOST_ALPHA = 120

    // Phase 2 — visual
    const val DAY_CYCLE_SECONDS = 60f       // full day/night cycle duration
    const val PARTICLE_MAX = 200            // max live particles
    const val SHAKE_INTENSITY_HIT = 22f
    const val SHAKE_DURATION_HIT = 0.35f
}
