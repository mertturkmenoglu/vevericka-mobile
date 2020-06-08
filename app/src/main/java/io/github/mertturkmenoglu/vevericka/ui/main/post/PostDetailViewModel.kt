package io.github.mertturkmenoglu.vevericka.ui.main.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.mertturkmenoglu.vevericka.data.model.Comment
import io.github.mertturkmenoglu.vevericka.data.model.Post
import io.github.mertturkmenoglu.vevericka.util.FirestoreHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger

class PostDetailViewModel : ViewModel(), AnkoLogger {
    private val mComments = MutableLiveData<List<Comment>>()

    fun getComments(post: Post): LiveData<List<Comment>> {
        mComments.postValue(post.comments)
        return mComments
    }

    fun addNewComment(post: Post, comment: Comment) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = FirestoreHelper.addNewComment(post, comment)

            if (result) {
                mComments.postValue(post.comments.toMutableList().apply { add(comment) })
            }
        }
    }
}