package com.ngaming.ngamingcase.posts.data.remote

import kotlinx.serialization.Serializable

/** API'den gelen JSON'un birebir karşılığı. */
@Serializable
internal data class PostDto(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)
