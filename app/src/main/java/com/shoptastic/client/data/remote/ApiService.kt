package com.shoptastic.client.data.remote

import com.shoptastic.client.data.model.request.login.LoginRequest
import com.shoptastic.client.data.model.response.login.LoginResponse
import com.shoptastic.client.data.model.response.products.ProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("products/categories")
    suspend fun getCategories(): Response<List<String>>

    @GET("products")
    suspend fun getProducts(): Response<List<ProductResponse>>

    @GET("products/category/{category_name}")
    suspend fun getProductsByCategory(
        @Path("category_name") categoryName: String
    ): Response<List<ProductResponse>>

    @GET("products/{id}")
    suspend fun getDetailProduct(
        @Path("id") id: String
    ):Response<ProductResponse>
}