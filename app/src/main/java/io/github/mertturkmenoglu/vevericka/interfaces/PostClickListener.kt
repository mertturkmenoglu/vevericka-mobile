package io.github.mertturkmenoglu.vevericka.interfaces

import io.github.mertturkmenoglu.vevericka.data.model.Post

interface PostClickListener {
    fun onCommentClick(post: Post)

    fun onFavClick(post: Post)

    fun onPersonClick(post: Post)
}