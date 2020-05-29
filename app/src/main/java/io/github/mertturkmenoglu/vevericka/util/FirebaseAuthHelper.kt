package io.github.mertturkmenoglu.vevericka.util

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthHelper {
    val instance: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val isLoggedIn: Boolean
        get() = instance.currentUser != null
}