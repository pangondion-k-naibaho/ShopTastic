package com.shoptastic.client.data.repository.products_saved

import com.shoptastic.client.data.model.other.ProductSaved

interface ProductSavedRepository {
    suspend fun insertProduct(product: ProductSaved)
    suspend fun getAllProducts(): List<ProductSaved>
    suspend fun deleteProduct(product: ProductSaved)
}