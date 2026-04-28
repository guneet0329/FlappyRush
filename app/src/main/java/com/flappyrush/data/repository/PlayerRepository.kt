package com.flappyrush.data.repository

import com.flappyrush.data.models.Player
import com.google.firebase.database.*

class PlayerRepository {

    private val db = FirebaseDatabase.getInstance()
    private val playersRef = db.getReference("players")

    suspend fun getPlayer(uid: String): Player? {
        var result: Player? = null
        playersRef.child(uid).get().addOnSuccessListener { snap ->
            result = snap.getValue(Player::class.java)
        }.await()
        return result
    }

    fun savePlayer(player: Player) {
        playersRef.child(player.uid).setValue(player)
    }

    fun updateBestScore(uid: String, score: Int) {
        val updates = mapOf<String, Any>(
            "bestScore" to score,
            "totalGames" to ServerValue.increment(1)
        )
        playersRef.child(uid).updateChildren(updates)
    }

    fun incrementGames(uid: String) {
        playersRef.child(uid).child("totalGames")
            .setValue(ServerValue.increment(1))
    }

    fun getLeaderboard(limit: Int = 100, onResult: (List<Player>) -> Unit) {
        playersRef.orderByChild("bestScore")
            .limitToLast(limit)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snap: DataSnapshot) {
                    val players = snap.children
                        .mapNotNull { it.getValue(Player::class.java) }
                        .sortedByDescending { it.bestScore }
                    onResult(players)
                }
                override fun onCancelled(error: DatabaseError) = onResult(emptyList())
            })
    }

    fun getPlayersByUids(uids: List<String>, onResult: (List<Player>) -> Unit) {
        val results = mutableListOf<Player>()
        var fetched = 0
        if (uids.isEmpty()) { onResult(emptyList()); return }
        uids.forEach { uid ->
            playersRef.child(uid).get().addOnSuccessListener { snap ->
                snap.getValue(Player::class.java)?.let { results.add(it) }
                fetched++
                if (fetched == uids.size) onResult(results.sortedByDescending { it.bestScore })
            }
        }
    }

    fun listenToPlayer(uid: String, onUpdate: (Player?) -> Unit): ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                onUpdate(snap.getValue(Player::class.java))
            }
            override fun onCancelled(error: DatabaseError) = onUpdate(null)
        }
        playersRef.child(uid).addValueEventListener(listener)
        return listener
    }

    fun removeListener(uid: String, listener: ValueEventListener) {
        playersRef.child(uid).removeEventListener(listener)
    }
}
