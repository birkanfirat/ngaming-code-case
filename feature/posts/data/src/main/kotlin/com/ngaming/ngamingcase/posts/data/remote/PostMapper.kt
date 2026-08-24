package com.ngaming.ngamingcase.posts.data.remote

import com.ngaming.ngamingcase.posts.domain.model.Post

/** DTO ile domain modeli arasında çeviri yapıyor. */
internal fun PostDto.toDomain() = Post(id = id, userId = userId, title = title, body = body)

internal fun Post.toDto() = PostDto(id = id, userId = userId, title = title, body = body)
