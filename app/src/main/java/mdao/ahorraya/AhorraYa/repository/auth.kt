package mdao.ahorraya.AhorraYa.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

object AuthManager {
    val auth: FirebaseAuth = Firebase.auth

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUser() = auth.currentUser
}