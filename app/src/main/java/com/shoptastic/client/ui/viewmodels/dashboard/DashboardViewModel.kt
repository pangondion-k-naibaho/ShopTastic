package com.shoptastic.client.ui.viewmodels.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoptastic.client.data.model.response.products.ProductResponse
import com.shoptastic.client.data.repository.categories.CategoriesRepository
import com.shoptastic.client.data.repository.products.ProductsRepository
import com.shoptastic.client.data.repository.productsbycategories.ProductsByCategoriesRepository
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val categoriesRepository: CategoriesRepository,
    private val productsRepository: ProductsRepository,
    private val productsByCategoriesRepository: ProductsByCategoriesRepository
): ViewModel() {
    private val TAG = DashboardViewModel::class.java.simpleName

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    private var _categoriesResponse = MutableLiveData<List<String>>()
    val categoriesResponse: LiveData<List<String>> = _categoriesResponse

    private var _productsResponse = MutableLiveData<List<ProductResponse>>()
    val productResponse: LiveData<List<ProductResponse>> = _productsResponse

    private var _productsByCategoriesResponse = MutableLiveData<List<ProductResponse>>()
    val productsByCategoriesResponse : LiveData<List<ProductResponse>> = _productsByCategoriesResponse

    fun getCategories(){
        _isLoading.value = true
        viewModelScope.launch {
            val result = categoriesRepository.getCategories()

            if(result.isSuccess){
                _categoriesResponse.value = result.getOrNull()
                _isFail.value = false
            }else{
                _isFail.value = true
                Log.e(TAG, "Error: ${result.exceptionOrNull()?.message}")
            }
            _isLoading.value = false
        }
    }

    fun getProducts(){
        _isLoading.value = true
        viewModelScope.launch {
            val result = productsRepository.getProducts()

            if(result.isSuccess){
                _productsResponse.value = result.getOrNull()
                _isFail.value = false
            }else{
                _isFail.value = true
                Log.e(TAG, "Error ${result.exceptionOrNull()?.message}")
            }
            _isLoading.value = false
        }
    }

    fun getProductsByCategory(categoryName: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = productsByCategoriesRepository.getProductsByCategories(categoryName)

            if(result.isSuccess){
                _productsByCategoriesResponse.value = result.getOrNull()
                _isFail.value = false
            }else{
                _isFail.value = true
                Log.e(TAG, "Error ${result.exceptionOrNull()?.message}")
            }
            _isLoading.value = false
        }
    }
}