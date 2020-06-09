package io.github.mertturkmenoglu.vevericka.ui.main.friendship

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.util.FirestoreHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class FriendshipRequestsViewModel : ViewModel(), AnkoLogger {
    private val mFriendshipRequests = MutableLiveData<List<User>>()

    fun getFriendshipRequests(uid: String): LiveData<List<User>> {
        viewModelScope.launch(Dispatchers.IO) {
            getFriendshipRequestsData(uid)
        }

        return mFriendshipRequests
    }

    fun approveFriendshipRequest(thisUser: String, otherUser: String) {
        viewModelScope.launch(Dispatchers.IO) {
            approveFriendshipRequestData(thisUser, otherUser)
        }
    }

    fun dismissFriendshipRequest(thisUser: String, otherUser: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dismissFriendshipRequestData(thisUser, otherUser)
        }
    }

    private suspend fun getFriendshipRequestsData(uid: String) {
        try {
            val result = FirestoreHelper.getFriendshipRequests(uid)
            mFriendshipRequests.postValue(result)
        } catch (e: Exception) {
            error { "GetFriendshipRequestsData failed: $e" }
        }
    }

    private suspend fun approveFriendshipRequestData(thisUser: String, otherUser: String) {
        try {
            FirestoreHelper.approveFriendshipRequest(thisUser, otherUser)
        } catch (e: Exception) {
            error { "ApproveFriendshipRequestData failed: $e" }
        }
    }

    private suspend fun dismissFriendshipRequestData(thisUser: String, otherUser: String) {
        try {
            FirestoreHelper.dismissFriendshipRequest(thisUser, otherUser)
        } catch (e: Exception) {
            error { "ApproveFriendshipRequestData failed: $e" }
        }
    }
}