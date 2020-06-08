package io.github.mertturkmenoglu.vevericka.data.model

import com.google.firebase.Timestamp
import java.util.*

data class Comment(
    val uid: String,
    val content: String,
    val timestamp: Timestamp = Timestamp(Calendar.getInstance().time)
) {
    // Needed for Firebase. Do not delete
    @Suppress("unused")
    constructor() : this("", "")
}