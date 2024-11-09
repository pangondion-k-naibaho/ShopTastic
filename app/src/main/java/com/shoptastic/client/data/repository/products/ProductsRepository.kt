package com.shoptastic.client.data.repository.products

import com.shoptastic.client.data.model.response.products.ProductResponse

interface ProductsRepository {
    suspend fun getProducts(): Result<List<ProductResponse>>
}