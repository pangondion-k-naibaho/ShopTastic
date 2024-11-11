package com.shoptastic.client.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shoptastic.client.data.local.dao.product_saved.ProductSavedDao
import com.shoptastic.client.data.model.converter.Converters
import com.shoptastic.client.data.model.other.ProductSaved

@Database(entities = [ProductSaved::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productSavedDao(): ProductSavedDao
}