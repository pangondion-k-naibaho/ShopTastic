package com.shoptastic.client.data.repository.productsbycategories

import com.shoptastic.client.data.model.response.products.ProductResponse
import com.shoptastic.client.data.remote.ApiService
import com.shoptastic.client.data.repository.products.ProductsRepository

class ProductsByCategoriesRepositoryImpl(private val apiService: ApiService): ProductsByCategoriesRepository {
    override suspend fun getProductsByCategories(categoryName: String): Result<List<ProductResponse>> {
        return try{
            val response = apiService.getProductsByCategory(categoryName)

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
        }catch (e:Exception){
            Result.failure(e)
        }
    }

}