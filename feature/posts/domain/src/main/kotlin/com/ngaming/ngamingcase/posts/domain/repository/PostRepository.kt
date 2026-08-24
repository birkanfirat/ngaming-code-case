package com.ngaming.ngamingcase.posts.domain.repository

import com.ngaming.ngamingcase.core.common.AppResult
import com.ngaming.ngamingcase.posts.domain.model.Post
import kotlinx.coroutines.flow.Flow

/** Gönderi işlemlerinin sözleşmesi. Bunu kimin nasıl yaptığını domain bilmiyor. */
interface PostRepository {

    val posts: Flow<List<Post>>

    fun post(id: Int): Flow<Post?>

    suspend fun refresh(): AppResult<Unit>

    suspend fun update(post: Post): AppResult<Post>

    suspend fun delete(post: Post): AppResult<Unit>

    suspend fun restore(post: Post, position: Int)
}
