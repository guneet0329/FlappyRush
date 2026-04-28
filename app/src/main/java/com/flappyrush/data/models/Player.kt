package com.flappyrush.data.models

data class Player(
    val uid: String = "",
    val username: String = "",
    val bestScore: Int = 0,
    val totalGames: Int = 0,
    val totalPipesPassed: Int = 0,
    val rank: String = "Bronze",       // Bronze, Silver, Gold, Diamond
    val seasonPoints: Int = 0,
    val skinId: String = "default",
    val createdAt: Long = System.currentTimeMillis()
)
