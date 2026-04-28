package com.flappyrush.data.repository

import com.flappyrush.data.models.Match

// Stub — Phase 4 will wire up Firebase
class MatchRepository {

    suspend fun createMatch(match: Match): String {
        // TODO Phase 4: push match to Firebase, return matchId
        return ""
    }

    suspend fun updateMatch(matchId: String, player1Score: Int? = null, player2Score: Int? = null, winnerId: String? = null) {
        // TODO Phase 4: partial update
    }

    suspend fun listenToMatch(matchId: String, onUpdate: (Match) -> Unit) {
        // TODO Phase 4: Firebase addValueEventListener
    }
}
