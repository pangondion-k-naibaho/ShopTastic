package com.shoptastic.client.ui.viewmodels.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoptastic.client.data.model.response.login.LoginResponse
import com.shoptastic.client.data.repository.login.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository):ViewModel() {
    private val TAG = LoginViewModel::class.java.simpleName

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail : LiveData<Boolean> = _isFail

    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    fun loginUser(username: String, password: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.loginUser(username, password)

            if(result.isSuccess){
                _loginResponse.value = result.getOrNull()
                _isFail.value = false
            }else{
                _isFail.value = true
                Log.e(TAG, "Error: ${result.exceptionOrNull()?.message}")
            }
            _isLoading.value = false
        }
    }
}