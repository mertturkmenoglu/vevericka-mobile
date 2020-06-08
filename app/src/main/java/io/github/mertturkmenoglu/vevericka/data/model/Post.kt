package io.github.mertturkmenoglu.vevericka.data.model

import com.google.firebase.Timestamp
import java.util.*

data class Post(
    val uid: String,
    val content: String,
    val imageUrl: String,
    val timestamp: Timestamp = Timestamp(Calendar.getInstance().time),
    val likeCount: Int = 0,
    val comments: List<Comment> = emptyList()
) {
    // Needed for Firebase. Do not delete.
    @Suppress("unused")
    constructor() : this("", "", "")
}