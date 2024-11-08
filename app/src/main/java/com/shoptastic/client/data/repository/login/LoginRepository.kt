package com.shoptastic.client.data.repository.login

import com.shoptastic.client.data.model.response.login.LoginResponse

interface LoginRepository {
    suspend fun loginUser(username: String, password: String): Result<LoginResponse>
}