package com.flappyrush.game.competitive

import com.flappyrush.data.models.Player
import com.flappyrush.data.repository.PlayerRepository

// Phase 3 — global and friends leaderboard
class Leaderboard(private val repository: PlayerRepository) {

    suspend fun getGlobalTop(limit: Int = 100): List<Player> {
        return repository.getLeaderboard(limit)
    }

    suspend fun getPlayerRank(uid: String): Int {
        // TODO Phase 3: query count of players with higher bestScore
        return -1
    }

    suspend fun getFriendsLeaderboard(friendUids: List<String>): List<Player> {
        // TODO Phase 3: filter leaderboard to friends
        return emptyList()
    }
}
