package com.shoptastic.client.ui.activity.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shoptastic.client.R
import com.shoptastic.client.data.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.shoptastic.client.data.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.shoptastic.client.ui.activity.dashboard.DashboardActivity
import com.shoptastic.client.ui.activity.login.LoginActivity
import com.shoptastic.client.ui.theme.ShopTasticTheme

class MainActivity : ComponentActivity() {
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appPreferences = this@MainActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val retrievedToken = appPreferences.getString(TOKEN_KEY, null)

        if(retrievedToken.isNullOrEmpty()){
            startActivity(LoginActivity.newIntent(this@MainActivity))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }else{
            startActivity(DashboardActivity.newIntent(this@MainActivity))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }

    }
}