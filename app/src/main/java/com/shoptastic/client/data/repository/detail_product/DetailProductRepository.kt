package com.shoptastic.client.data.repository.detail_product

import com.shoptastic.client.data.model.response.products.ProductResponse

interface DetailProductRepository {
    suspend fun getDetailProduct(id: String): Result<ProductResponse>
}