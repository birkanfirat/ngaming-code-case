package com.ngaming.ngamingcase.core.network

import com.ngaming.ngamingcase.core.common.AppError
import com.ngaming.ngamingcase.core.common.AppResult
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

/** Retrofit çağrısını sarmalıyor. Ağ hatasını AppError'a çeviriyor, iptali olduğu gibi geçiriyor. */
suspend fun <T> apiCall(block: suspend () -> T): AppResult<T> = try {
    AppResult.Success(block())
} catch (cancellation: CancellationException) {
    throw cancellation
} catch (http: HttpException) {
    AppResult.Failure(AppError.Server(http.code()))
} catch (io: IOException) {
    AppResult.Failure(AppError.Network)
} catch (unexpected: Exception) {
    AppResult.Failure(AppError.Unknown)
}
