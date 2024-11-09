package com.shoptastic.client.data.repository.productsbycategories

import com.shoptastic.client.data.model.response.products.ProductResponse

interface ProductsByCategoriesRepository {
    suspend fun getProductsByCategories(categoryName: String): Result<List<ProductResponse>>
}