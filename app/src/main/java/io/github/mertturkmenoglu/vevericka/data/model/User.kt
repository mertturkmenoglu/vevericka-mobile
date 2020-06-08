package io.github.mertturkmenoglu.vevericka.data.model

import com.google.firebase.Timestamp
import io.github.mertturkmenoglu.vevericka.util.Constants
import java.util.*

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val profileVisibility: String = Constants.ProfileVisibility.PUBLIC,
    val createDate: Timestamp = Timestamp(Calendar.getInstance().time),
    val postCount: Int = 0,
    val imageUrl: String = "defaultProfilePicture",
    val friends: List<String> = emptyList(),
    val pendingFriendRequests: List<String> = emptyList(),
    val bio: String = "",
    val location: String = "",
    val website: String = ""
) {
    // Needed for Firebase
    @Suppress("unused")
    constructor() : this("", "", "")

    fun getFullName() = "${firstName.trim()} ${lastName.trim()}"
}