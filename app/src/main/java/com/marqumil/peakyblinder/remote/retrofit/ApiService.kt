package com.marqumil.peakyblinder.remote.retrofit

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.marqumil.peakyblinder.remote.post.ArticleBody
import com.marqumil.peakyblinder.remote.post.GlucoseBody
import com.marqumil.peakyblinder.remote.post.LoginBody
import com.marqumil.peakyblinder.remote.post.RegisterBody
import com.marqumil.peakyblinder.remote.response.ArtikelResponse
import com.marqumil.peakyblinder.remote.response.GlucoseResponse
import com.marqumil.peakyblinder.remote.response.ListResponse
import com.marqumil.peakyblinder.remote.response.LoginResponse
import com.marqumil.peakyblinder.remote.response.ObjectResponse
import com.marqumil.peakyblinder.remote.response.PhotoResponse
import com.marqumil.peakyblinder.remote.response.PostArticleResponse
import com.marqumil.peakyblinder.remote.response.RetinopathyResponse
import com.marqumil.peakyblinder.remote.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("/auth/login")
    suspend fun postLogin(
        @Body loginBody: LoginBody
    ): ObjectResponse<LoginResponse>

    @GET("/auth/me")
    suspend fun getUser(): ObjectResponse<UserResponse>

    @POST("/auth/register")
    suspend fun postRegister(
        @Body loginBody: RegisterBody
    ): ObjectResponse<LoginResponse>

    @POST("/auth/logout")
    suspend fun getLogout(): ObjectResponse<Boolean>

    @GET("/glucometer/")
    suspend fun getGlucose(): ObjectResponse<GlucoseResponse>

    @POST("/glucometer/")
    suspend fun getGlucose(
        @Query("date") date: String
    ): ObjectResponse<GlucoseResponse>

    @POST("/glucometer/")
    suspend fun postGlucose(
        @Body glucoseResponse: GlucoseBody
    ): ObjectResponse<GlucoseResponse>

    @GET("/article")
    suspend fun getArticleAll(): ListResponse<ArtikelResponse>

    @POST("/article")
    suspend fun postArticle(
        @Body articleBody: ArticleBody
    ): ObjectResponse<PostArticleResponse>

    @POST("/article/upload")
    suspend fun postArticlePhoto(
        @Part image: MultipartBody.Part
    ): ObjectResponse<PhotoResponse>

    @Multipart
    @POST("/")
    suspend fun postRetinopathy(
        @Part file: MultipartBody.Part
    ): RetinopathyResponse
}