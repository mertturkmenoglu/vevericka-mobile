package io.github.mertturkmenoglu.vevericka.interfaces

import io.github.mertturkmenoglu.vevericka.data.model.User

interface PersonClickListener {
    fun onClick(user: User)
}