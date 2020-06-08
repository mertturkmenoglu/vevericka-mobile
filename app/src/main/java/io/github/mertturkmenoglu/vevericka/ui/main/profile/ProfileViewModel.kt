package io.github.mertturkmenoglu.vevericka.ui.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.util.FirestoreHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {
    private companion object {
        private const val TAG = "ProfileViewModel"
    }

    private var mUser = MutableLiveData<User>()

    fun getUser(uid: String): LiveData<User> {
        viewModelScope.launch(Dispatchers.IO) {
            getUserData(uid)
        }

        return mUser
    }

    private suspend fun getUserData(uid: String) {
        try {
            val result = FirestoreHelper.getUserAsTask(uid).await()
            val user = result.toObject<User>()
            mUser.postValue(user)
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "getUserData: ", e)
        }
    }

}