package com.ngaming.ngamingcase.posts.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/** jsonplaceholder uç noktaları. */
internal interface PostApi {

    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: PostDto): PostDto

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int)
}
