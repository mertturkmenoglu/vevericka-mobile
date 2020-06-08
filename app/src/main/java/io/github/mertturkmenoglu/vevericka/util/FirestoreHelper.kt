package io.github.mertturkmenoglu.vevericka.util

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.github.mertturkmenoglu.vevericka.data.model.User
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.verbose

object FirestoreHelper : AnkoLogger {
    val instance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val users: CollectionReference by lazy { instance.collection(Constants.Collections.USERS) }

    fun saveUser(user: User): Task<Void> {
        val auth = FirebaseAuthHelper.instance
        val currentUser = auth.currentUser ?: throw IllegalStateException()
        val ref = instance.collection(Constants.Collections.USERS).document(currentUser.uid)

        return ref.set(user)
    }

    fun getUser(uid: String): Task<DocumentSnapshot> {
        return users.document(uid).get()
    }

    fun updateImageUrl(uid: String, newPath: String): Task<Void> {
        verbose("Image Url update for $uid")
        return users.document(uid).update(Constants.UserFields.IMAGE_URL, newPath)
    }
}