package com.flappyrush.data.models

data class Match(
    val matchId: String = "",
    val player1Uid: String = "",
    val player2Uid: String = "",
    val player1Score: Int = 0,
    val player2Score: Int = 0,
    val winnerId: String = "",
    val seed: Long = 0L,               // same seed = same pipe layout for both players
    val mode: MatchMode = MatchMode.ONE_V_ONE,
    val startedAt: Long = System.currentTimeMillis(),
    val endedAt: Long = 0L
)

enum class MatchMode {
    ONE_V_ONE,
    TOURNAMENT
}
