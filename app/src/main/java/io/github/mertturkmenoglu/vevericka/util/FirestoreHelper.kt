package io.github.mertturkmenoglu.vevericka.util

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import io.github.mertturkmenoglu.vevericka.data.User

object FirestoreHelper {
    val instance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun saveUser(user: User): Task<Void> {
        val auth = FirebaseAuthHelper.instance
        val currentUser = auth.currentUser ?: throw IllegalStateException()
        val ref = instance.collection(Constants.Collections.USERS).document(currentUser.uid)

        return ref.set(user)
    }
}