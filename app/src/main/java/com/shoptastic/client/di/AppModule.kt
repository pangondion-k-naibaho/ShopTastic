package com.shoptastic.client.di

import com.shoptastic.client.data.remote.ApiConfig
import com.shoptastic.client.data.repository.login.LoginRepository
import com.shoptastic.client.data.repository.login.LoginRepositoryImpl
import com.shoptastic.client.ui.viewmodels.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { ApiConfig.createApiService() }
}

val repositoryModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get())}
}

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
}