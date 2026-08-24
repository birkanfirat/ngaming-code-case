package com.ngaming.ngamingcase.posts.domain.model

/** Uygulamanın gönderi modeli. */
data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)
