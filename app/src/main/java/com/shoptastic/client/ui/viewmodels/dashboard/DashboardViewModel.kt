package com.shoptastic.client.ui.viewmodels.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoptastic.client.data.repository.categories.CategoriesRepository
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val categoriesRepository: CategoriesRepository
): ViewModel() {
    private val TAG = DashboardViewModel::class.java.simpleName

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    private var _categoriesResponse = MutableLiveData<List<String>>()
    val categoriesResponse: LiveData<List<String>> = _categoriesResponse

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
}