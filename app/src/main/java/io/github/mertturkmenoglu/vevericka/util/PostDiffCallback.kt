package io.github.mertturkmenoglu.vevericka.util

import androidx.recyclerview.widget.DiffUtil
import io.github.mertturkmenoglu.vevericka.data.model.Post

object PostDiffCallback {
    val callback = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}