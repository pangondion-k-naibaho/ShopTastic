package com.shoptastic.client.data.repository.categories

interface CategoriesRepository {
    suspend fun getCategories(): Result<List<String>>
}