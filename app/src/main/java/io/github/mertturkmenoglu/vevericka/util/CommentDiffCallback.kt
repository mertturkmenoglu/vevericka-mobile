package io.github.mertturkmenoglu.vevericka.util

import androidx.recyclerview.widget.DiffUtil
import io.github.mertturkmenoglu.vevericka.data.model.Comment

object CommentDiffCallback {
    val callback = object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.uid == newItem.uid && oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }
}