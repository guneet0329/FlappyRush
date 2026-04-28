package com.flappyrush.data.models

data class Player(
    val uid: String = "",
    val username: String = "",
    val bestScore: Int = 0,
    val totalGames: Int = 0,
    val totalPipesPassed: Int = 0,
    val rank: String = "Bronze",
    val seasonPoints: Int = 0,
    val skinId: String = "GOLDEN",
    val pipeThemeId: String = "CLASSIC",
    val friendUids: Map<String, Boolean> = emptyMap(),
    val createdAt: Long = System.currentTimeMillis()
) {
    // Needed for Firebase deserialization
    constructor() : this("")

    fun getRankFromScore(): String = when {
        bestScore >= 100 -> "Diamond"
        bestScore >= 50  -> "Gold"
        bestScore >= 20  -> "Silver"
        else             -> "Bronze"
    }
}
