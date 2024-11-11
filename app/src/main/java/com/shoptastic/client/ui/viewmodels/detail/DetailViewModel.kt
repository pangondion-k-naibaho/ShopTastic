package com.shoptastic.client.ui.viewmodels.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoptastic.client.data.model.other.ProductSaved
import com.shoptastic.client.data.model.response.products.ProductResponse
import com.shoptastic.client.data.repository.detail_product.DetailProductRepository
import com.shoptastic.client.data.repository.products_saved.ProductSavedRepository
import com.shoptastic.client.data.repository.products_saved.SavedProductRepository
import kotlinx.coroutines.launch

class DetailViewModel(
    private val detailProductRepository: DetailProductRepository,
//    private val productSavedRepository: ProductSavedRepository,
    private val savedProductRepository: SavedProductRepository
): ViewModel() {
    private val TAG = DetailViewModel::class.java.simpleName

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    private var _isSaveSuccess = MutableLiveData<Boolean>()
    val isSaveSuccess: LiveData<Boolean> = _isSaveSuccess

    private var _detailProductResponse = MutableLiveData<ProductResponse>()
    val detailProductResponse: LiveData<ProductResponse> = _detailProductResponse

    fun getDetailProduct(id: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = detailProductRepository.getDetailProduct(id)

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

    fun saveProduct(product: ProductResponse) {
        _isLoading.value = true // Menandakan proses loading
        viewModelScope.launch {
            try {
                savedProductRepository.insertProduct(product)
                _isFail.value = false // Berhasil menyimpan produk
                _isSaveSuccess.value = true
            } catch (e: Exception) {
                _isFail.value = true // Terjadi kesalahan saat menyimpan
                Log.e(TAG, "Error saving product: ${e.message}")
            } finally {
                _isLoading.value = false // Menandakan proses selesai
            }
        }
    }
}