package com.ngaming.ngamingcase.posts.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngaming.ngamingcase.core.common.onFailure
import com.ngaming.ngamingcase.core.common.onSuccess
import com.ngaming.ngamingcase.posts.domain.model.Post
import com.ngaming.ngamingcase.posts.domain.usecase.DeletePostUseCase
import com.ngaming.ngamingcase.posts.domain.usecase.GetPostsUseCase
import com.ngaming.ngamingcase.posts.domain.usecase.RefreshPostsUseCase
import com.ngaming.ngamingcase.posts.domain.usecase.RestorePostUseCase
import com.ngaming.ngamingcase.posts.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Liste ekranının beyni. Yükleme, silme ve geri alma buradan yönetiliyor. */
@HiltViewModel
class PostListViewModel @Inject constructor(
    getPosts: GetPostsUseCase,
    private val refreshPosts: RefreshPostsUseCase,
    private val deletePost: DeletePostUseCase,
    private val restorePost: RestorePostUseCase,
) : ViewModel() {

    private val posts = getPosts().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val loading = MutableStateFlow(Loading())

    private val uiPosts = posts
        .map { posts -> posts.map { it.toUiModel() } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val uiState: StateFlow<PostListUiState> = combine(uiPosts, loading) { posts, loading ->
        PostListUiState(
            posts = posts,
            isLoading = loading.initial,
            isRefreshing = loading.refreshing,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), PostListUiState())

    private val eventChannel = Channel<PostListEvent>(Channel.BUFFERED)
    val events: Flow<PostListEvent> = eventChannel.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        load(userInitiated = false)
    }

    fun onRefresh() = load(userInitiated = true)

    fun onPostSwiped(postId: Int) {
        val position = posts.value.indexOfFirst { it.id == postId }
        val post = posts.value.getOrNull(position) ?: return

        viewModelScope.launch {
            deletePost(post)
                .onSuccess { eventChannel.send(PostListEvent.PostDeleted(post, position)) }
                .onFailure { eventChannel.send(PostListEvent.Failed(it)) }
        }
    }

    fun onUndoDelete(post: Post, position: Int) {
        viewModelScope.launch { restorePost(post, position) }
    }

    private fun load(userInitiated: Boolean) {
        if (loadJob?.isActive == true) return

        loadJob = viewModelScope.launch {
            loading.value = Loading(initial = !userInitiated, refreshing = userInitiated)
            refreshPosts().onFailure { eventChannel.send(PostListEvent.Failed(it)) }
            loading.value = Loading(initial = false, refreshing = false)
        }
    }

    private data class Loading(val initial: Boolean = true, val refreshing: Boolean = false)

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
