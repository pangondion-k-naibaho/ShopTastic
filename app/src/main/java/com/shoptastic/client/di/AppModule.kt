package com.shoptastic.client.di

import androidx.room.Room
import com.shoptastic.client.data.local.database.AppDatabase
import com.shoptastic.client.data.remote.ApiConfig
import com.shoptastic.client.data.repository.categories.CategoriesRepository
import com.shoptastic.client.data.repository.categories.CategoriesRepositoryImpl
import com.shoptastic.client.data.repository.detail_product.DetailProductRepository
import com.shoptastic.client.data.repository.detail_product.DetailProductRepositoryImpl
import com.shoptastic.client.data.repository.login.LoginRepository
import com.shoptastic.client.data.repository.login.LoginRepositoryImpl
import com.shoptastic.client.data.repository.products.ProductsRepository
import com.shoptastic.client.data.repository.products.ProductsRepositoryImpl
import com.shoptastic.client.data.repository.products_saved.ProductSavedRepository
import com.shoptastic.client.data.repository.products_saved.ProductSavedRepositoryImpl
import com.shoptastic.client.data.repository.products_saved.SavedProductRepository
import com.shoptastic.client.data.repository.products_saved.SavedProductRepositoryImpl
import com.shoptastic.client.data.repository.productsbycategories.ProductsByCategoriesRepository
import com.shoptastic.client.data.repository.productsbycategories.ProductsByCategoriesRepositoryImpl
import com.shoptastic.client.ui.viewmodels.dashboard.DashboardViewModel
import com.shoptastic.client.ui.viewmodels.detail.DetailViewModel
import com.shoptastic.client.ui.viewmodels.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { ApiConfig.createApiService() }
}

val repositoryModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get())}
    single<CategoriesRepository> { CategoriesRepositoryImpl(get()) }
    single<ProductsRepository> { ProductsRepositoryImpl(get()) }
    single<ProductsByCategoriesRepository> { ProductsByCategoriesRepositoryImpl(get()) }
    single<DetailProductRepository> { DetailProductRepositoryImpl(get()) }
//    single<ProductSavedRepository> { ProductSavedRepositoryImpl(get()) }
    single<SavedProductRepository> { SavedProductRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { DashboardViewModel(get(), get(), get()) }
    viewModel { DetailViewModel(get(), get()) }
}

val databaseModule = module {
    // Membuat instance database
    single {
        Room.databaseBuilder(
            get(), // `get()` ini untuk mengambil konteks dari Koin
            AppDatabase::class.java,
            "app_database" // Nama database Anda
        ).build()
    }

    // Menyediakan DAO dari database
//    single { get<AppDatabase>().productSavedDao() }

    single { get<AppDatabase>().savedProductDao() }
}