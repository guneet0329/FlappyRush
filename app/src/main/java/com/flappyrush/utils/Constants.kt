package com.flappyrush.utils

object Constants {
    // Physics
    const val GRAVITY = 1800f          // px/s²
    const val FLAP_VELOCITY = -620f    // px/s (negative = upward)
    const val MAX_FALL_VELOCITY = 900f // terminal velocity px/s

    // Pipes
    const val PIPE_WIDTH = 120f
    const val PIPE_GAP = 320f          // vertical gap between top/bottom pipe
    const val PIPE_SPEED_INITIAL = 300f // px/s
    const val PIPE_SPEED_MAX = 520f
    const val PIPE_SPEED_INCREMENT = 5f // per pipe passed
    const val PIPE_SPACING = 520f      // horizontal distance between pipe pairs

    // Bird
    const val BIRD_WIDTH = 68f
    const val BIRD_HEIGHT = 52f
    const val BIRD_START_X_RATIO = 0.25f  // 25% from left of screen
    const val BIRD_ROTATION_UP = -25f     // degrees when flapping
    const val BIRD_ROTATION_DOWN = 45f    // degrees when falling

    // Game
    const val TARGET_FPS = 60
    const val GAME_TICK_MS = (1000 / TARGET_FPS).toLong()

    // Scoring
    const val SCORE_PER_PIPE = 1
    const val BEST_SCORE_KEY = "best_score"

    // Ghost bird (Phase 4)
    const val GHOST_ALPHA = 120  // 0-255, semi-transparent
}
