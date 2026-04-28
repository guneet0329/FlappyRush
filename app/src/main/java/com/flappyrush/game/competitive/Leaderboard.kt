package com.flappyrush.game.competitive

import com.flappyrush.data.models.Player
import com.flappyrush.data.repository.PlayerRepository

class Leaderboard(private val repository: PlayerRepository) {

    enum class Tab { GLOBAL, FRIENDS }

    fun getGlobal(limit: Int = 100, onResult: (List<LeaderboardEntry>) -> Unit) {
        repository.getLeaderboard(limit) { players ->
            onResult(players.mapIndexed { i, p ->
                LeaderboardEntry(rank = i + 1, player = p, isCurrentUser = false)
            })
        }
    }

    fun getFriends(currentUid: String, friendUids: List<String>, onResult: (List<LeaderboardEntry>) -> Unit) {
        val uidsToFetch = (friendUids + currentUid).distinct()
        repository.getPlayersByUids(uidsToFetch) { players ->
            onResult(players.mapIndexed { i, p ->
                LeaderboardEntry(rank = i + 1, player = p, isCurrentUser = p.uid == currentUid)
            })
        }
    }

    fun getPlayerRank(uid: String, onResult: (Int) -> Unit) {
        repository.getLeaderboard(1000) { players ->
            val rank = players.indexOfFirst { it.uid == uid } + 1
            onResult(if (rank == 0) -1 else rank)
        }
    }

    data class LeaderboardEntry(
        val rank: Int,
        val player: Player,
        val isCurrentUser: Boolean
    )
}
