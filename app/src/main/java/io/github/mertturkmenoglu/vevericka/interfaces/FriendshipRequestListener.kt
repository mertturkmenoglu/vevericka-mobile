package io.github.mertturkmenoglu.vevericka.interfaces

import io.github.mertturkmenoglu.vevericka.data.model.User

interface FriendshipRequestListener {
    interface OnApproveListener {
        fun onApprove(user: User)
    }

    interface OnDismissListener {
        fun onDismiss(user: User)
    }
}