package io.github.mertturkmenoglu.vevericka.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.mertturkmenoglu.vevericka.data.model.Post
import io.github.mertturkmenoglu.vevericka.util.FirestoreHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class HomeViewModel : ViewModel(), AnkoLogger {
    private var mPosts = MutableLiveData<List<Post>>()

    fun getPosts(uid: String): LiveData<List<Post>> {
        viewModelScope.launch(Dispatchers.IO) {
            getPostsData(uid)
        }

        return mPosts
    }

    private suspend fun getPostsData(uid: String) {
        try {
            val result = FirestoreHelper.getAllPosts(uid)
            mPosts.postValue(result.sortedByDescending { it.timestamp })
        } catch (e: Exception) {
            error { "GetPostsData failed: $e" }
        }
    }
}