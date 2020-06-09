package io.github.mertturkmenoglu.vevericka.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.mertturkmenoglu.vevericka.data.model.Post
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.util.FirestoreHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class ProfileViewModel : ViewModel(), AnkoLogger {
    private val mUser = MutableLiveData<User>()
    private val mPosts = MutableLiveData<List<Post>>()

    fun getUser(uid: String): LiveData<User> {
        viewModelScope.launch(Dispatchers.IO) {
            getUserData(uid)
        }

        return mUser
    }

    fun getPosts(uid: String): LiveData<List<Post>> {
        viewModelScope.launch(Dispatchers.IO) {
            getPostsData(uid)
        }

        return mPosts
    }

    suspend fun sendFriendshipRequest(from: String, to: String): Boolean {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            sendFriendshipRequestData(from, to)
        }
    }

    private suspend fun getUserData(uid: String) {
        try {
            val result = FirestoreHelper.getUser(uid)
            mUser.postValue(result)
        } catch (e: Exception) {
            error { "GetUsersData failed: $e" }
        }
    }

    private suspend fun getPostsData(uid: String) {
        try {
            val result = FirestoreHelper.getUserPosts(uid)
            mPosts.postValue(result)
        } catch (e: Exception) {
            error { "GetPostsData failed: $e" }
        }
    }

    private suspend fun sendFriendshipRequestData(from: String, to: String): Boolean {
        return try {
            FirestoreHelper.sendFriendshipRequest(from, to)
        } catch (e: Exception) {
            error { "SendFriendshipRequestData failed: $e" }
            false
        }
    }
}