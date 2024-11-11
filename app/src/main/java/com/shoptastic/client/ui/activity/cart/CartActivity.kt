package com.shoptastic.client.ui.activity.cart

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shoptastic.client.R
import com.shoptastic.client.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private val TAG = CartActivity::class.java.simpleName
    private lateinit var binding: ActivityCartBinding

    companion object{
        fun newIntent(context: Context): Intent = Intent(context, CartActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
    }

    private fun setUpView(){

    }
}