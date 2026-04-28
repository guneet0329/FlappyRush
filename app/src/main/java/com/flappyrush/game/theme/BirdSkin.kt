package com.flappyrush.game.theme

import android.graphics.Color

enum class BirdSkin(
    val bodyColor: Int,
    val wingColor: Int,
    val beakColor: Int,
    val trailColor: Int,
    val displayName: String
) {
    GOLDEN(
        bodyColor  = Color.parseColor("#FFD700"),
        wingColor  = Color.parseColor("#FFA500"),
        beakColor  = Color.parseColor("#FF6B35"),
        trailColor = Color.parseColor("#FFD700"),
        displayName = "Golden"
    ),
    NEON_BLUE(
        bodyColor  = Color.parseColor("#00E5FF"),
        wingColor  = Color.parseColor("#0091EA"),
        beakColor  = Color.parseColor("#FF6D00"),
        trailColor = Color.parseColor("#00E5FF"),
        displayName = "Neon Blue"
    ),
    CRIMSON(
        bodyColor  = Color.parseColor("#FF1744"),
        wingColor  = Color.parseColor("#D50000"),
        beakColor  = Color.parseColor("#FFD740"),
        trailColor = Color.parseColor("#FF1744"),
        displayName = "Crimson"
    ),
    GHOST(
        bodyColor  = Color.parseColor("#E0E0E0"),
        wingColor  = Color.parseColor("#BDBDBD"),
        beakColor  = Color.parseColor("#FF8F00"),
        trailColor = Color.parseColor("#FFFFFF"),
        displayName = "Ghost"
    ),
    EMERALD(
        bodyColor  = Color.parseColor("#00E676"),
        wingColor  = Color.parseColor("#00C853"),
        beakColor  = Color.parseColor("#FF6D00"),
        trailColor = Color.parseColor("#00E676"),
        displayName = "Emerald"
    )
}
