package com.flappyrush.auth

import com.flappyrush.data.models.Player
import com.flappyrush.data.repository.PlayerRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthManager {

    private val auth = FirebaseAuth.getInstance()
    private val playerRepo = PlayerRepository()

    val currentUser: FirebaseUser? get() = auth.currentUser
    val isLoggedIn: Boolean get() = currentUser != null
    val uid: String? get() = currentUser?.uid

    fun signUp(email: String, password: String, username: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user ?: return@addOnSuccessListener
                val player = Player(
                    uid = user.uid,
                    username = username,
                    createdAt = System.currentTimeMillis()
                )
                playerRepo.savePlayer(player)
                onResult(true, null)
            }
            .addOnFailureListener { onResult(false, it.message) }
    }

    fun signIn(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, it.message) }
    }

    fun signOut() = auth.signOut()

    fun signInAnonymously(onResult: (Boolean) -> Unit) {
        auth.signInAnonymously()
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                val player = Player(uid = uid, username = "Guest_${uid.take(5)}")
                playerRepo.savePlayer(player)
                onResult(true)
            }
            .addOnFailureListener { onResult(false) }
    }
}
