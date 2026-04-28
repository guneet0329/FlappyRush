package com.flappyrush.data.models

data class Match(
    val matchId: String = "",
    val player1Uid: String = "",
    val player2Uid: String = "",
    val player1Score: Int = 0,
    val player2Score: Int = 0,
    val player1Alive: Boolean = true,
    val player2Alive: Boolean = true,
    val winnerId: String = "",
    val seed: Long = 0L,
    val mode: MatchMode = MatchMode.ONE_V_ONE,
    val status: MatchStatus = MatchStatus.WAITING,
    val startedAt: Long = System.currentTimeMillis(),
    val endedAt: Long = 0L
) {
    constructor() : this("")

    fun isFinished() = status == MatchStatus.FINISHED
    fun getWinnerUid() = when {
        player1Score > player2Score -> player1Uid
        player2Score > player1Score -> player2Uid
        else -> ""  // draw
    }
}

enum class MatchMode { ONE_V_ONE, TOURNAMENT }

enum class MatchStatus { WAITING, PLAYING, FINISHED }
