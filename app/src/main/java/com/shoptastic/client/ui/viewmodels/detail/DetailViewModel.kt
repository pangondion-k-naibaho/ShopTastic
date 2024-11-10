package com.shoptastic.client.ui.viewmodels.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoptastic.client.data.model.response.products.ProductResponse
import com.shoptastic.client.data.repository.detail_product.DetailProductRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: DetailProductRepository): ViewModel() {
    private val TAG = DetailViewModel::class.java.simpleName

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    private var _detailProductResponse = MutableLiveData<ProductResponse>()
    val detailProductResponse: LiveData<ProductResponse> = _detailProductResponse

    fun getDetailProduct(id: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.getDetailProduct(id)

            if(result.isSuccess){
                _detailProductResponse.value = result.getOrNull()
                _isFail.value = false
            }else{
                _isFail.value = true
                Log.e(TAG, "Error: ${result.exceptionOrNull()?.message}")
            }
            _isLoading.value = false
        }
    }
}