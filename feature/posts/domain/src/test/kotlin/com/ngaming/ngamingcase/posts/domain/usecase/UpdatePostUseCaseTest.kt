package com.ngaming.ngamingcase.posts.domain.usecase

import com.ngaming.ngamingcase.core.common.AppError
import com.ngaming.ngamingcase.core.common.AppResult
import com.ngaming.ngamingcase.posts.domain.model.Post
import com.ngaming.ngamingcase.posts.domain.repository.PostRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/** Düzenleme kurallarının testleri. */
class UpdatePostUseCaseTest {

    private val repository = mockk<PostRepository>(relaxed = true)
    private val updatePost = UpdatePostUseCase(repository)

    private val post = Post(id = 1, userId = 1, title = "title", body = "body")

    @Test
    fun `rejects a blank title without touching the repository`() = runTest {
        val result = updatePost(post, title = "   ", body = "new body")

        assertEquals(AppResult.Failure(AppError.Validation(AppError.Validation.Field.TITLE)), result)
        coVerify(exactly = 0) { repository.update(any()) }
    }

    @Test
    fun `skips the request when nothing changed`() = runTest {
        val result = updatePost(post, title = " title ", body = " body ")

        assertEquals(AppResult.Success(PostUpdate.Unchanged), result)
        coVerify(exactly = 0) { repository.update(any()) }
    }

    @Test
    fun `sends trimmed values to the repository`() = runTest {
        val expected = post.copy(title = "edited", body = "edited body")
        coEvery { repository.update(expected) } returns AppResult.Success(expected)

        val result = updatePost(post, title = "  edited  ", body = "  edited body  ")

        assertEquals(AppResult.Success(PostUpdate.Saved(expected)), result)
        coVerify { repository.update(expected) }
    }
}
