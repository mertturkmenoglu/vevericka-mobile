package io.github.mertturkmenoglu.vevericka.interfaces

import io.github.mertturkmenoglu.vevericka.data.model.User

interface FriendshipRequestListener {
    fun onApprove(user: User)

    fun onDismiss(user: User)
}