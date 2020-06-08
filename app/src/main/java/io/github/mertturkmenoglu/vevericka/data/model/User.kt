package io.github.mertturkmenoglu.vevericka.data.model

import com.google.firebase.Timestamp
import io.github.mertturkmenoglu.vevericka.util.Constants
import java.util.*

@Suppress("unused")
data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val profileVisibility: String = Constants.ProfileVisibility.PUBLIC,
    val createDate: Timestamp = Timestamp(Calendar.getInstance().time),
    val postCount: Int = 0,
    val imageUrl: String = "defaultProfilePicture",
    val friendsCount: Int = 0,
    val bio: String = "",
    val location: String = "",
    val website: String = ""
) {
    constructor() : this("", "", "") {
        // Needed for Firebase
    }

    fun getFullName() = "${firstName.trim()} ${lastName.trim()}"
}