package com.shoptastic.client.data.repository.products

import com.shoptastic.client.data.model.response.products.ProductResponse
import com.shoptastic.client.data.remote.ApiService

class ProductsRepositoryImpl(private val apiService: ApiService): ProductsRepository {
    override suspend fun getProducts(): Result<List<ProductResponse>> {
        return try{
            val response = apiService.getProducts()

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