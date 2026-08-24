package com.ngaming.ngamingcase.posts.domain.usecase

import com.ngaming.ngamingcase.core.common.AppError
import com.ngaming.ngamingcase.core.common.AppResult
import com.ngaming.ngamingcase.core.common.map
import com.ngaming.ngamingcase.posts.domain.model.Post
import com.ngaming.ngamingcase.posts.domain.repository.PostRepository
import javax.inject.Inject

/** Düzenlemenin sonucu: ya kaydedildi ya da değişen bir şey yoktu. */
sealed interface PostUpdate {
    data object Unchanged : PostUpdate

    data class Saved(val post: Post) : PostUpdate
}

/** Düzenlenen alanları kontrol edip kaydediyor. Boş alanı reddediyor, değişiklik yoksa isteği hiç atmıyor. */
class UpdatePostUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    suspend operator fun invoke(post: Post, title: String, body: String): AppResult<PostUpdate> {
        val newTitle = title.trim()
        val newBody = body.trim()
        return when {
            newTitle.isEmpty() -> AppResult.Failure(AppError.Validation(AppError.Validation.Field.TITLE))
            newBody.isEmpty() -> AppResult.Failure(AppError.Validation(AppError.Validation.Field.BODY))
            newTitle == post.title && newBody == post.body -> AppResult.Success(PostUpdate.Unchanged)
            else -> repository.update(post.copy(title = newTitle, body = newBody)).map(PostUpdate::Saved)
        }
    }
}
