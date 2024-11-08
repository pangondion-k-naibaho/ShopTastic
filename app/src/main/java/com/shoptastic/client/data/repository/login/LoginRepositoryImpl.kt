package com.shoptastic.client.data.repository.login

import com.shoptastic.client.data.model.request.login.LoginRequest
import com.shoptastic.client.data.model.response.login.LoginResponse
import com.shoptastic.client.data.remote.ApiService

class LoginRepositoryImpl(private val apiService: ApiService): LoginRepository {
    override suspend fun loginUser(username: String, password: String): Result<LoginResponse> {
        return try{
            val request = LoginRequest(username, password)

            val response = apiService.loginUser(request)

            if(response.isSuccessful){
                val body = response.body()
                if(body != null){
                    Result.success(body)
                }else{
                    Result.failure(Exception("Response body is null"))
                }
            }else{
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e:Exception){
            Result.failure(e)
        }
    }
}