package com.shoptastic.client.ui.viewmodels.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoptastic.client.data.model.response.products.ProductResponse
import com.shoptastic.client.data.repository.products_saved.SavedProductRepository
import kotlinx.coroutines.launch

class CartViewModel(
    private val savedProductRepository: SavedProductRepository
): ViewModel() {
    private val TAG = CartViewModel::class.java.simpleName

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    private var _savedProductsResponse = MutableLiveData<List<ProductResponse>>()
    val savedProductsResponse : LiveData<List<ProductResponse>> = _savedProductsResponse

    fun getSavedProducts(){
        _isLoading.value = true
        viewModelScope.launch {
            try{
                _savedProductsResponse.value = savedProductRepository.getAllProducts()
                _isFail.value = false
            }catch (e:Exception){
                _isFail.value = true
                Log.e(TAG, "Error retrieving saved products: ${e.message}")
            }finally {
                _isLoading.value = false
            }
        }
    }
}