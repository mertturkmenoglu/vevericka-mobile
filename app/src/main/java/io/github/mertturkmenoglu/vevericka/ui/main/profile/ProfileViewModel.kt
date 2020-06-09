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
}