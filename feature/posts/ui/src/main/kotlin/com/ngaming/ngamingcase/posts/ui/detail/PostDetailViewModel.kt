package com.ngaming.ngamingcase.posts.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngaming.ngamingcase.core.common.onFailure
import com.ngaming.ngamingcase.core.common.onSuccess
import com.ngaming.ngamingcase.posts.domain.usecase.GetPostUseCase
import com.ngaming.ngamingcase.posts.domain.usecase.PostUpdate
import com.ngaming.ngamingcase.posts.domain.usecase.UpdatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Detay ekranının beyni. Düzenleme modu ve kaydetme burada yönetiliyor. */
@HiltViewModel
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getPost: GetPostUseCase,
    private val updatePost: UpdatePostUseCase,
) : ViewModel() {

    private val postId: Int = checkNotNull(savedStateHandle[PostDetailFragment.ARG_POST_ID]) {}

    private val editing = MutableStateFlow(false)
    private val saving = MutableStateFlow(false)

    val uiState: StateFlow<PostDetailUiState> =
        combine(getPost(postId), editing, saving) { post, isEditing, isSaving ->
            PostDetailUiState(post = post, isEditing = isEditing, isSaving = isSaving)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), PostDetailUiState())

    private val eventChannel = Channel<PostDetailEvent>(Channel.BUFFERED)
    val events: Flow<PostDetailEvent> = eventChannel.receiveAsFlow()

    fun onEditClick() {
        editing.value = true
    }

    fun onCancelClick() {
        editing.value = false
    }

    fun onSaveClick(title: String, body: String) {
        val post = uiState.value.post ?: return
        if (saving.value) return

        viewModelScope.launch {
            saving.value = true
            updatePost(post, title, body)
                .onSuccess { update ->
                    editing.value = false
                    eventChannel.send(
                        when (update) {
                            PostUpdate.Unchanged -> PostDetailEvent.Unchanged
                            is PostUpdate.Saved -> PostDetailEvent.Saved
                        },
                    )
                }
                .onFailure { eventChannel.send(PostDetailEvent.Failed(it)) }
            saving.value = false
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
