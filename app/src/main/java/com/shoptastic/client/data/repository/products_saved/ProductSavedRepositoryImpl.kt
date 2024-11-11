package com.shoptastic.client.data.repository.products_saved

import com.shoptastic.client.data.local.dao.product_saved.ProductSavedDao
import com.shoptastic.client.data.model.other.ProductSaved

class ProductSavedRepositoryImpl(private val productDao: ProductSavedDao) : ProductSavedRepository {

    override suspend fun insertProduct(product: ProductSaved) {
        productDao.insertProduct(product)
    }

    override suspend fun getAllProducts(): List<ProductSaved> {
        return productDao.getAllProducts()
    }

    override suspend fun deleteProduct(product: ProductSaved) {
        productDao.deleteProduct(product)
    }
}