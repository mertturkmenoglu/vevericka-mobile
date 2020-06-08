package io.github.mertturkmenoglu.vevericka.interfaces

import io.github.mertturkmenoglu.vevericka.data.model.Post

interface PostClickListener {
    fun onClick(post: Post)
}