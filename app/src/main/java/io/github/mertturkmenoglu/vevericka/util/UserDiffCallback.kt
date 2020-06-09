package io.github.mertturkmenoglu.vevericka.util

import androidx.recyclerview.widget.DiffUtil
import io.github.mertturkmenoglu.vevericka.data.model.User

object UserDiffCallback {
    val callback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}