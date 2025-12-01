package com.example.report

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Part
import okhttp3.MultipartBody
import org.json.JSONObject

interface SimpleApi {

    @Multipart
    @POST("v1/keywords")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        //@Part("desc") desc: RequestBody
    ): Response<Post>

    @Multipart
    @POST("v1/keywords?")
    suspend fun uploadImage2(
        @Part image: MultipartBody.Part,
        @Part("num_keywords") number: Int
    ): Response<Post>

    @GET("v1/keywords?url=http://image.everypixel.com/2014.12/67439828186edc79b9be81a4dedea8b03c09a12825b_b.jpg&num_keywords=10")
    suspend fun getPost(): Response<Post>
/*
    @POST("v1/keywords?url=http://image.everypixel.com/2014.12/67439828186edc79b9be81a4dedea8b03c09a12825b_b.jpg&num_keywords=10")
    suspend fun getPost(): Response<JSONObject>
    */

    @GET("posts/1")
    suspend fun getPost2(
        @Path("postNumber") number: Int
    ): Response<Post>

    @POST("posts")
    suspend fun pushPost(
        @Body post: Post
    ): Response<Post>
}