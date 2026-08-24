package com.ngaming.ngamingcase.posts.ui.list

import com.ngaming.ngamingcase.core.common.AppError
import com.ngaming.ngamingcase.core.common.AppResult
import com.ngaming.ngamingcase.posts.domain.model.Post
import com.ngaming.ngamingcase.posts.domain.usecase.DeletePostUseCase
import com.ngaming.ngamingcase.posts.domain.usecase.GetPostsUseCase
import com.ngaming.ngamingcase.posts.domain.usecase.RefreshPostsUseCase
import com.ngaming.ngamingcase.posts.domain.usecase.RestorePostUseCase
import com.ngaming.ngamingcase.posts.ui.testing.FakePostRepository
import com.ngaming.ngamingcase.posts.ui.testing.MainDispatcherRule
import com.ngaming.ngamingcase.posts.ui.testing.collectInBackground
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

/** Liste ekranı akışlarının testleri. */
class PostListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakePostRepository()

    private fun viewModel() = PostListViewModel(
        getPosts = GetPostsUseCase(repository),
        refreshPosts = RefreshPostsUseCase(repository),
        deletePost = DeletePostUseCase(repository),
        restorePost = RestorePostUseCase(repository),
    )

    @Test
    fun `loads posts on start`() = runTest {
        repository.remotePosts = listOf(post(1), post(2))
        val viewModel = viewModel()
        collectInBackground(viewModel.uiState)

        val state = viewModel.uiState.value

        assertEquals(listOf(1, 2), state.posts.map { it.id })
        assertFalse(state.isLoading)
    }

    @Test
    fun `reports a failed refresh`() = runTest {
        repository.refreshResult = AppResult.Failure(AppError.Network)

        val event = viewModel().events.first()

        assertEquals(PostListEvent.Failed(AppError.Network), event)
    }

    @Test
    fun `swiped post is removed and undo puts it back`() = runTest {
        repository.remotePosts = listOf(post(1), post(2), post(3))
        val viewModel = viewModel()
        collectInBackground(viewModel.uiState)

        viewModel.onPostSwiped(postId = 2)

        val event = viewModel.events.first() as PostListEvent.PostDeleted
        assertEquals(post(2), event.post)
        assertEquals(1, event.position)
        assertEquals(listOf(1, 3), viewModel.uiState.value.posts.map { it.id })

        viewModel.onUndoDelete(event.post, event.position)

        assertEquals(listOf(1, 2, 3), viewModel.uiState.value.posts.map { it.id })
    }

    private fun post(id: Int) = Post(id = id, userId = 1, title = "title $id", body = "body $id")
}
