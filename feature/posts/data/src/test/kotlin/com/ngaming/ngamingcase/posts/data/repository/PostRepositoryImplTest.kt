package com.ngaming.ngamingcase.posts.data.repository

import com.ngaming.ngamingcase.core.common.AppError
import com.ngaming.ngamingcase.core.common.AppResult
import com.ngaming.ngamingcase.posts.data.remote.PostApi
import com.ngaming.ngamingcase.posts.data.remote.PostDto
import com.ngaming.ngamingcase.posts.domain.model.Post
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

/** Repository'nin cache ve geri alma davranışının testleri. */
class PostRepositoryImplTest {

    private val api = FakePostApi()
    private val repository = PostRepositoryImpl(api)

    @Test
    fun `refresh publishes the posts returned by the api`() = runTest {
        api.posts = listOf(dto(1), dto(2))

        val result = repository.refresh()

        assertEquals(AppResult.Success(Unit), result)
        assertEquals(listOf(post(1), post(2)), repository.posts.first())
    }

    @Test
    fun `delete puts the post back when the call fails`() = runTest {
        api.posts = listOf(dto(1), dto(2), dto(3))
        repository.refresh()
        api.failure = IOException("offline")

        val result = repository.delete(post(2))

        assertEquals(AppResult.Failure(AppError.Network), result)
        assertEquals(listOf(post(1), post(2), post(3)), repository.posts.first())
    }

    @Test
    fun `update replaces the post in place`() = runTest {
        api.posts = listOf(dto(1), dto(2))
        repository.refresh()
        val edited = post(2).copy(title = "edited")

        val result = repository.update(edited)

        assertEquals(AppResult.Success(edited), result)
        assertEquals(listOf(post(1), edited), repository.posts.first())
    }

    private fun dto(id: Int) = PostDto(id = id, userId = 1, title = "title $id", body = "body $id")

    private fun post(id: Int) = Post(id = id, userId = 1, title = "title $id", body = "body $id")

    private class FakePostApi : PostApi {
        var posts: List<PostDto> = emptyList()
        var failure: Exception? = null

        override suspend fun getPosts(): List<PostDto> {
            failure?.let { throw it }
            return posts
        }

        override suspend fun updatePost(id: Int, post: PostDto): PostDto {
            failure?.let { throw it }
            return post
        }

        override suspend fun deletePost(id: Int) {
            failure?.let { throw it }
        }
    }
}
