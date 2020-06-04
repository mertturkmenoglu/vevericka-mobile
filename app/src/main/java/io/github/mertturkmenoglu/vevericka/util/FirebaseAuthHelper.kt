package io.github.mertturkmenoglu.vevericka.util

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthHelper {
    val instance: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val isLoggedIn: Boolean
        get() = instance.currentUser != null

    fun createUser(email: String, password: String): Task<AuthResult> {
        return instance.createUserWithEmailAndPassword(email, password)
    }

    fun signIn(email: String, password: String): Task<AuthResult> {
        return instance.signInWithEmailAndPassword(email, password)
    }
}