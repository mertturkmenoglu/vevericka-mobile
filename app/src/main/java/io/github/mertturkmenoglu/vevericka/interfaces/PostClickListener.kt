package io.github.mertturkmenoglu.vevericka.interfaces

import io.github.mertturkmenoglu.vevericka.data.model.Post

interface PostClickListener {
    interface OnCommentClickListener {
        fun onCommentClick(post: Post)
    }

    interface OnFavClickListener {
        fun onFavClick(post: Post)
    }

    interface OnPersonClickListener {
        fun onPersonClick(post: Post)
    }
}