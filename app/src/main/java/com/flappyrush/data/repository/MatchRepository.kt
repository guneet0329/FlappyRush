package com.flappyrush.data.repository

import com.flappyrush.data.models.Match
import com.flappyrush.data.models.MatchMode
import com.google.firebase.database.*

class MatchRepository {

    private val db = FirebaseDatabase.getInstance()
    private val matchesRef = db.getReference("matches")
    private val queueRef   = db.getReference("matchmaking_queue")

    fun createMatch(match: Match, onResult: (String) -> Unit) {
        val ref = matchesRef.push()
        val matchId = ref.key ?: return
        ref.setValue(match.copy(matchId = matchId))
            .addOnSuccessListener { onResult(matchId) }
    }

    fun updateMatch(matchId: String, updates: Map<String, Any>) {
        matchesRef.child(matchId).updateChildren(updates)
    }

    fun listenToMatch(matchId: String, onUpdate: (Match?) -> Unit): ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                onUpdate(snap.getValue(Match::class.java))
            }
            override fun onCancelled(error: DatabaseError) = onUpdate(null)
        }
        matchesRef.child(matchId).addValueEventListener(listener)
        return listener
    }

    fun removeMatchListener(matchId: String, listener: ValueEventListener) {
        matchesRef.child(matchId).removeEventListener(listener)
    }

    // Matchmaking queue
    fun joinQueue(uid: String, onOpponentFound: (opponentUid: String, matchId: String, seed: Long) -> Unit) {
        queueRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(data: MutableData): Transaction.Result {
                val waiting = data.children.firstOrNull()
                if (waiting != null && waiting.key != uid) {
                    // Match found — create match
                    val opponentUid = waiting.key ?: return Transaction.abort()
                    val seed = System.currentTimeMillis()
                    val match = Match(
                        player1Uid = opponentUid,
                        player2Uid = uid,
                        seed = seed,
                        mode = MatchMode.ONE_V_ONE
                    )
                    createMatch(match) { matchId ->
                        onOpponentFound(opponentUid, matchId, seed)
                    }
                    data.child(opponentUid).value = null  // remove from queue
                } else {
                    data.child(uid).value = System.currentTimeMillis()  // add self to queue
                }
                return Transaction.success(data)
            }
            override fun onComplete(error: DatabaseError?, committed: Boolean, snap: DataSnapshot?) {}
        })
    }

    fun leaveQueue(uid: String) = queueRef.child(uid).removeValue()

    // Push opponent position (for ghost bird)
    fun pushPosition(matchId: String, uid: String, y: Float, score: Int) {
        db.getReference("match_positions/$matchId/$uid").setValue(
            mapOf("y" to y, "score" to score, "ts" to ServerValue.TIMESTAMP)
        )
    }

    fun listenToOpponentPosition(matchId: String, opponentUid: String, onUpdate: (Float, Int) -> Unit): ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                val y = snap.child("y").getValue(Float::class.java) ?: return
                val score = snap.child("score").getValue(Int::class.java) ?: 0
                onUpdate(y, score)
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        db.getReference("match_positions/$matchId/$opponentUid").addValueEventListener(listener)
        return listener
    }
}
