package io.github.mertturkmenoglu.vevericka.ui.main.friends

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

class FriendsViewModel : ViewModel(), AnkoLogger {
    private val mFriends = MutableLiveData<List<User>>()

    fun getFriends(uid: String): LiveData<List<User>> {
        viewModelScope.launch(Dispatchers.IO) {
            getFriendsData(uid)
        }

        return mFriends
    }

    private suspend fun getFriendsData(uid: String) {
        try {
            val result = FirestoreHelper.getFriends(uid)
            mFriends.postValue(result.sortedByDescending { it.getFullName() })
        } catch (e: Exception) {
            error { "GetFriendsData failed: $e" }
        }
    }
}