package io.github.mertturkmenoglu.vevericka.ui.main.search

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

class SearchViewModel : ViewModel(), AnkoLogger {
    private val mResults = MutableLiveData<List<User>>()

    fun getSearchResults(query: String): LiveData<List<User>> {
        viewModelScope.launch(Dispatchers.IO) {
            getResultsData(query)
        }

        return mResults
    }

    private suspend fun getResultsData(query: String) {
        try {
            val result = FirestoreHelper.searchUsers(query)
            mResults.postValue(result)
        } catch (e: Exception) {
            error { "GetResultsData failed: $e" }
        }
    }
}