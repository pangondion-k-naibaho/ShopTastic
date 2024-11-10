package com.shoptastic.client.data.repository.detail_product

import com.shoptastic.client.data.model.response.products.ProductResponse
import com.shoptastic.client.data.remote.ApiService

class DetailProductRepositoryImpl(private val apiService: ApiService): DetailProductRepository{
    override suspend fun getDetailProduct(id: String): Result<ProductResponse> {
        return try {
            val response = apiService.getDetailProduct(id)

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