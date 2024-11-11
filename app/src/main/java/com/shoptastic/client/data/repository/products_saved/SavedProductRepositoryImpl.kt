package com.shoptastic.client.data.repository.products_saved

import com.shoptastic.client.data.local.dao.product_saved.ProductSavedDao
import com.shoptastic.client.data.local.dao.product_saved.SavedProductDao
import com.shoptastic.client.data.model.other.ProductSaved
import com.shoptastic.client.data.model.response.products.ProductResponse

class SavedProductRepositoryImpl(private val productDao: SavedProductDao) : SavedProductRepository {

    override suspend fun insertProduct(product: ProductResponse) {
        productDao.insertProduct(product)
    }

    override suspend fun getAllProducts(): List<ProductResponse> {
        return productDao.getAllProducts()
    }

    override suspend fun deleteProduct(product: ProductResponse) {
        productDao.deleteProduct(product)
    }
}