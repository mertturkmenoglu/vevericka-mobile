package io.github.mertturkmenoglu.vevericka.util

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreHelper {
    val instance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun saveUser(email: String): Task<Void> {
        val auth = FirebaseAuthHelper.instance
        val user = auth.currentUser ?: throw IllegalStateException()
        val ref = instance.collection(Constants.Collections.USERS).document(user.uid)

        val map = hashMapOf(Constants.UserFields.email to email)

        return ref.set(map)
    }
}