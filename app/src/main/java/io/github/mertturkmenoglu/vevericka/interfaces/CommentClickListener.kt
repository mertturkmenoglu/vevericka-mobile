package io.github.mertturkmenoglu.vevericka.interfaces

interface CommentClickListener {
    fun onClick(uid: String)

    fun onClick(action: (uid: String) -> Unit)
}