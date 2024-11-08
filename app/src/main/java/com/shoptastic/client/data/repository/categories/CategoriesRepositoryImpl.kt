package com.shoptastic.client.data.repository.categories

import com.shoptastic.client.data.remote.ApiService

class CategoriesRepositoryImpl(private val apiService: ApiService): CategoriesRepository {
    override suspend fun getCategories(): Result<List<String>> {
        return try{
            val response = apiService.getCategories()

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
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}