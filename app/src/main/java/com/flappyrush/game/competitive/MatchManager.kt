package com.flappyrush.game.competitive

import com.flappyrush.auth.AuthManager
import com.flappyrush.data.models.Match
import com.flappyrush.data.models.MatchStatus
import com.flappyrush.data.repository.MatchRepository
import com.google.firebase.database.ValueEventListener

class MatchManager {

    private val auth        = AuthManager()
    private val matchRepo   = MatchRepository()

    var currentMatch: Match? = null
    var opponentUid: String = ""
    var isPlayer1: Boolean = false

    var onMatchFound: ((matchId: String, seed: Long) -> Unit)? = null
    var onOpponentPosition: ((y: Float, score: Int) -> Unit)? = null
    var onMatchFinished: ((winnerId: String) -> Unit)? = null
    var onSearchTimeout: (() -> Unit)? = null

    private var matchListener: ValueEventListener? = null
    private var positionListener: ValueEventListener? = null
    private var searchTimeoutRunnable: Runnable? = null

    private val uid get() = auth.uid ?: ""

    // --- Matchmaking ---

    fun findMatch() {
        matchRepo.joinQueue(uid) { opponentUid, matchId, seed ->
            this.opponentUid = opponentUid
            this.isPlayer1 = false  // we joined, opponent created
            listenToMatch(matchId)
            onMatchFound?.invoke(matchId, seed)
        }
    }

    fun cancelSearch() {
        matchRepo.leaveQueue(uid)
        searchTimeoutRunnable = null
    }

    // --- In-match ---

    fun reportScore(matchId: String, score: Int, y: Float) {
        matchRepo.pushPosition(matchId, uid, y, score)

        val myAliveKey  = if (isPlayer1) "player1Alive" else "player2Alive"
        val myScoreKey  = if (isPlayer1) "player1Score" else "player2Score"
        matchRepo.updateMatch(matchId, mapOf(myScoreKey to score))
    }

    fun reportDeath(matchId: String, finalScore: Int) {
        val myAliveKey = if (isPlayer1) "player1Alive" else "player2Alive"
        val myScoreKey = if (isPlayer1) "player1Score" else "player2Score"
        matchRepo.updateMatch(matchId, mapOf(
            myAliveKey to false,
            myScoreKey to finalScore
        ))
        checkMatchEnd(matchId)
    }

    private fun checkMatchEnd(matchId: String) {
        val match = currentMatch ?: return
        val bothDead = !match.player1Alive && !match.player2Alive
        if (bothDead) {
            val winnerId = match.getWinnerUid()
            matchRepo.updateMatch(matchId, mapOf(
                "status"    to MatchStatus.FINISHED.name,
                "winnerId"  to winnerId,
                "endedAt"   to System.currentTimeMillis()
            ))
        }
    }

    fun listenToOpponent(matchId: String) {
        positionListener = matchRepo.listenToOpponentPosition(matchId, opponentUid) { y, score ->
            onOpponentPosition?.invoke(y, score)
        }
    }

    private fun listenToMatch(matchId: String) {
        matchListener = matchRepo.listenToMatch(matchId) { match ->
            match ?: return@listenToMatch
            currentMatch = match
            if (match.isFinished()) {
                onMatchFinished?.invoke(match.winnerId)
            }
        }
    }

    fun cleanup(matchId: String) {
        matchListener?.let { matchRepo.removeMatchListener(matchId, it) }
        matchListener = null
        positionListener = null
        currentMatch = null
    }
}
