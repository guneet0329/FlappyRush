package com.flappyrush.game.theme

import android.graphics.Color

enum class PipeTheme(
    val bodyColor: Int,
    val capColor: Int,
    val rimColor: Int,
    val displayName: String
) {
    CLASSIC(
        bodyColor   = Color.parseColor("#4CAF50"),
        capColor    = Color.parseColor("#388E3C"),
        rimColor    = Color.parseColor("#2E7D32"),
        displayName = "Classic"
    ),
    STEEL(
        bodyColor   = Color.parseColor("#78909C"),
        capColor    = Color.parseColor("#546E7A"),
        rimColor    = Color.parseColor("#37474F"),
        displayName = "Steel"
    ),
    CANDY(
        bodyColor   = Color.parseColor("#EC407A"),
        capColor    = Color.parseColor("#C2185B"),
        rimColor    = Color.parseColor("#880E4F"),
        displayName = "Candy"
    ),
    GOLD(
        bodyColor   = Color.parseColor("#FFA000"),
        capColor    = Color.parseColor("#FF6F00"),
        rimColor    = Color.parseColor("#E65100"),
        displayName = "Gold"
    )
}
