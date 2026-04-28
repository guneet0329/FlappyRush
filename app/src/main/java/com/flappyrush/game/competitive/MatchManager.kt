package com.flappyrush.game.competitive

import com.flappyrush.data.models.Match
import com.flappyrush.data.models.MatchMode
import com.flappyrush.data.repository.MatchRepository

// Phase 4 — orchestrates matchmaking and live match state
class MatchManager(private val repository: MatchRepository) {

    var currentMatch: Match? = null
    var onMatchFound: ((Match) -> Unit)? = null
    var onOpponentScoreUpdate: ((Int) -> Unit)? = null

    fun findMatch(playerUid: String) {
        // TODO Phase 4:
        // 1. Write to Firebase "matchmaking_queue" with playerUid + timestamp
        // 2. Listen for another player to join
        // 3. First player creates Match with shared seed
        // 4. Both players receive the match and start simultaneously
    }

    fun reportScore(matchId: String, score: Int, playerUid: String) {
        // TODO Phase 4: push score update to Firebase
    }

    fun cancelSearch() {
        // TODO Phase 4: remove from queue
    }
}
