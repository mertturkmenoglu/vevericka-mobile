package io.github.mertturkmenoglu.vevericka.util

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import io.github.mertturkmenoglu.vevericka.data.model.Comment
import io.github.mertturkmenoglu.vevericka.data.model.Post
import io.github.mertturkmenoglu.vevericka.data.model.User
import kotlinx.coroutines.tasks.await
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
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

    fun getUserAsTask(uid: String): Task<DocumentSnapshot> {
        return users.document(uid).get()
    }

    suspend fun getUser(uid: String): User {
        return users.document(uid).get().await().toObject<User>() ?: throw ClassCastException()
    }

    fun updateImageUrl(uid: String, newPath: String): Task<Void> {
        verbose("Image Url update for $uid")
        return users.document(uid).update(Constants.UserFields.IMAGE_URL, newPath)
    }

    fun addPost(uid: String, post: Post): Task<Void> {
        return users.document(uid).collection(Constants.Collections.POSTS).document().set(post)
    }

    suspend fun getUserPosts(uid: String): List<Post> {
        return try {
            users.document(uid)
                .collection(Constants.Collections.POSTS)
                .get()
                .await()
                .toObjects()
        } catch (e: Exception) {
            error { "GetUserPosts failed: $e" }
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getFriendsPosts(uid: String): List<Post> {
        return try {
            getUser(uid).friends.flatMap {
                getUserPosts(it)
            }
        } catch (e: Exception) {
            error { "GetFriendsPosts failed: $e" }
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getAllPosts(uid: String): List<Post> {
        return try {
            getUserPosts(uid).union(getFriendsPosts(uid)).toList()
        } catch (e: Exception) {
            error { "GetAllPosts failed: $e" }
            emptyList()
        }
    }

    suspend fun getPostReference(post: Post): DocumentReference {
        return users.document(post.uid)
            .collection(Constants.Collections.POSTS)
            .get()
            .await()
            .first {
                post.uid == it.getString("uid")
                        && it.getTimestamp("timestamp")?.equals(post.timestamp) == true
            }
            .reference
    }

    suspend fun addNewComment(post: Post, comment: Comment): Boolean {
        return try {
            val updatedComments = post.comments.toMutableList().apply { add(comment) }
            getPostReference(post).update("comments", updatedComments).await()
            true
        } catch (e: Exception) {
            error { "AddNewComment failed: $e" }
            e.printStackTrace()
            false
        }
    }

    suspend fun getFriends(uid: String): List<User> {
        return try {
            getUser(uid).friends.map { getUser(it) }
        } catch (e: Exception) {
            error { "GetFriends failed: $e" }
            e.printStackTrace()
            emptyList()
        }
    }
}