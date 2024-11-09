package com.shoptastic.client.di

import com.shoptastic.client.data.remote.ApiConfig
import com.shoptastic.client.data.repository.categories.CategoriesRepository
import com.shoptastic.client.data.repository.categories.CategoriesRepositoryImpl
import com.shoptastic.client.data.repository.login.LoginRepository
import com.shoptastic.client.data.repository.login.LoginRepositoryImpl
import com.shoptastic.client.data.repository.products.ProductsRepository
import com.shoptastic.client.data.repository.products.ProductsRepositoryImpl
import com.shoptastic.client.data.repository.productsbycategories.ProductsByCategoriesRepository
import com.shoptastic.client.data.repository.productsbycategories.ProductsByCategoriesRepositoryImpl
import com.shoptastic.client.ui.viewmodels.dashboard.DashboardViewModel
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
}

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { DashboardViewModel(get(), get(), get()) }
}