package com.umangmathur.umangdemoapp.network

import com.umangmathur.umangdemoapp.model.User.User
import com.umangmathur.umangdemoapp.model.UserPost.UserPost
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceholderApi {

    @GET("/users/{userid}")
    fun getUser(@Path("userid") id: Int): Observable<User>

    @GET("/users")
    fun getAllUsers(): Observable<ArrayList<User>>

    @GET("/posts")
    fun getPostsForuser(@Query("userId") userId : Int) : Observable<ArrayList<UserPost>>

}