package com.example.githubuserinfo.api

import com.example.githubuserinfo.model.UserDetailInfo
import com.example.githubuserinfo.model.UserInfo
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface GithubService {

    companion object {
        const val GITHUB_BASE_URL = "https://api.github.com"
    }

    /**
     * @param since get users since #since(id)
     * @param perPage get #perpage users at one time
     */
    @GET("/users")
    fun getUsers (
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): Call<List<UserInfo>>

    /**
     * get user data with url
     */
    @GET
    fun getUsers (
        @Url url: String
    ): Single<Response<List<UserInfo>>>


    @GET("/users/{username}")
    fun getUserDetail (
        @Path(value = "username") username: String
    ): Single<UserDetailInfo>
}