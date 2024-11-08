package com.shoptastic.client.data.remote

import com.shoptastic.client.data.model.request.login.LoginRequest
import com.shoptastic.client.data.model.response.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>
}