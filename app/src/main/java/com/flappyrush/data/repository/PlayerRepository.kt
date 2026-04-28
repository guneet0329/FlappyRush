package com.flappyrush.data.repository

import com.flappyrush.data.models.Player

// Stub — Phase 3 will wire up Firebase
class PlayerRepository {

    suspend fun getPlayer(uid: String): Player? {
        // TODO Phase 3: fetch from Firebase Realtime Database
        return null
    }

    suspend fun savePlayer(player: Player) {
        // TODO Phase 3: push to Firebase
    }

    suspend fun updateBestScore(uid: String, score: Int) {
        // TODO Phase 3: atomic update with Firebase transaction
    }

    suspend fun getLeaderboard(limit: Int = 100): List<Player> {
        // TODO Phase 3: query ordered by bestScore desc
        return emptyList()
    }
}
