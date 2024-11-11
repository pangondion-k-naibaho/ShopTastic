package com.shoptastic.client.data.repository.products_saved

import com.shoptastic.client.data.model.other.ProductSaved
import com.shoptastic.client.data.model.response.products.ProductResponse

interface SavedProductRepository {
    suspend fun insertProduct(product: ProductResponse)
    suspend fun getAllProducts(): List<ProductResponse>
    suspend fun deleteProduct(product: ProductResponse)
}