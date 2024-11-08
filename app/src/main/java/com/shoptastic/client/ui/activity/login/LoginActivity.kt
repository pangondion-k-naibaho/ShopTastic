    package com.shoptastic.client.ui.activity.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.shoptastic.client.R
import com.shoptastic.client.data.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.shoptastic.client.data.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.shoptastic.client.databinding.ActivityLoginBinding
import com.shoptastic.client.ui.activity.dashboard.DashboardActivity
import com.shoptastic.client.ui.custom_components.InputTextView
import com.shoptastic.client.ui.custom_components.PopUpNotificationListener
import com.shoptastic.client.ui.custom_components.showPopUpNotification
import com.shoptastic.client.ui.viewmodels.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val TAG = LoginActivity::class.java.simpleName
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModel()

    companion object{
        fun newIntent(context: Context):Intent = Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeStatus()

        setUpView()
    }

    private fun observeStatus(){
        loginViewModel.isLoading.observe(this@LoginActivity, {
            setLayoutForLoading(it)
        })

        loginViewModel.isFail.observe(this@LoginActivity, {
            if(!it){
                Toast.makeText(this@LoginActivity, getString(R.string.toastLoginSuccess), Toast.LENGTH_SHORT).show()
            }else{
                setLayoutForPopUp(true)
                showPopUpNotification(
                    textTitle = getString(R.string.dialogTxtLoginFailedTitle),
                    textDesc = getString(R.string.dialogTxtLoginFailedDesc),
                    backgroundImage = R.drawable.ic_fail,
                    listener = object: PopUpNotificationListener{
                        override fun onPopUpClosed() {
                            setLayoutForPopUp(false)
                        }
                    }
                )
            }
        })

        loginViewModel.loginResponse.observe(this@LoginActivity, {response->
            val appPreferences = this@LoginActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            val editor = appPreferences.edit()

            lifecycleScope.launch(Dispatchers.IO){
                editor.putString(TOKEN_KEY, response.token)
                editor.apply()

                withContext(Dispatchers.Main){
                    startActivity(DashboardActivity.newIntent(this@LoginActivity))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                }
            }
        })
    }

    private fun setUpView(){
        binding.apply {
            itvUsername.apply {
                setTitle(getString(R.string.itvTxtUsername))
                setInputType(InputTextView.INPUT_TYPE.REGULAR)
            }

            itvPassword.apply {
                setTitle(getString(R.string.itvTxtPassword))
                setInputType(InputTextView.INPUT_TYPE.PASSWORD)

                val listener = object: InputTextView.InputTextListener{
                    override fun onClickReveal() {
                        revealPassword()
                    }
                }

                setListener(listener)
            }

            btnLogin.apply {
                setOnClickListener {
                    if(isFormComplete()){
                        val retrievedUsername = binding.itvUsername.getText()
                        val retrievedPassword = binding.itvPassword.getText()

                        Log.d(TAG, "username: $retrievedUsername, password: $retrievedPassword")
                        loginViewModel.loginUser(retrievedUsername, retrievedPassword)
                    }
                }
            }
        }
    }

    private fun isFormComplete(): Boolean{
        val listItv = listOf(
            binding.itvUsername,
            binding.itvPassword
        )

        var result = true

        for(itv in listItv){
            if(itv.getText().isNullOrEmpty()){
                itv.setOnBlankWarning()
                result = false
            }
        }

        return result
    }

    private fun setLayoutForLoading(isLoading: Boolean){
        if(isLoading) {
            binding.loadingLayout.visibility = View.VISIBLE
            binding.pbLoading.visibility = View.VISIBLE
        } else {
            binding.loadingLayout.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE
        }
    }

    private fun setLayoutForPopUp(isShown: Boolean){
        if(isShown){
            binding.loadingLayout.visibility = View.VISIBLE
            binding.pbLoading.visibility = View.GONE
        }else{
            binding.loadingLayout.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE
        }
    }

}