package com.shoptastic.client.ui.activity.cart

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.shoptastic.client.R
import com.shoptastic.client.databinding.ActivityCartBinding
import com.shoptastic.client.ui.activity.detail.DetailActivity
import com.shoptastic.client.ui.custom_components.PopUpNotificationListener
import com.shoptastic.client.ui.custom_components.showPopUpNotification
import com.shoptastic.client.ui.rv_adapters.ItemSavedProductAdapter
import com.shoptastic.client.ui.viewmodels.cart.CartViewModel
import com.shoptastic.client.utils.Extension.Companion.toCurrencyString
import com.shoptastic.client.utils.Extension.Companion.toProductSavedWithCount
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartActivity : AppCompatActivity() {
    private val TAG = CartActivity::class.java.simpleName
    private lateinit var binding: ActivityCartBinding
    private lateinit var rvAdapter : ItemSavedProductAdapter
    private val cartViewModel: CartViewModel by viewModel()

    private var _totalPrice = MutableLiveData<Double>(0.0)
    private var totalPrice: LiveData<Double> = _totalPrice

    companion object{
        fun newIntent(context: Context): Intent = Intent(context, CartActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeStatus()

        setUpView()
    }

    private fun observeStatus(){
        cartViewModel.isLoading.observe(this@CartActivity, {
            setLayoutForLoading(it)
        })

        cartViewModel.isFail.observe(this@CartActivity, {
            if(it){
                setLayoutForDialog(true)
                showPopUpNotification(
                    textTitle = getString(R.string.dialogTxtRetrievingFailedTitle),
                    textDesc = getString(R.string.dialogTxtRetrievingFailedDesc),
                    backgroundImage = R.drawable.ic_fail,
                    listener = object: PopUpNotificationListener {
                        override fun onPopUpClosed() {
                            setLayoutForDialog(false)
                        }
                    }
                )
            }
        })

        totalPrice.observe(this@CartActivity, {
            Log.d(TAG, "total Price: ${it}")
            val price = it.toCurrencyString()
            binding.tvTotalPriceValue.text = price
        })
    }

    private fun setUpView(){
        setSupportActionBar(binding.toolbarCart)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setDisplayShowTitleEnabled(false)
        }

        cartViewModel.getSavedProducts()

        cartViewModel.savedProductsResponse.observe(this@CartActivity, {response->

            binding.rvSavedProduct.apply {
                rvAdapter = ItemSavedProductAdapter(
                    response.toProductSavedWithCount().toMutableList(),
                    object: ItemSavedProductAdapter.ItemListener{
                        override fun onBoxChecked(price: Double) {
                            _totalPrice.value = _totalPrice.value!! + price
                        }

                        override fun onBoxUnchecked(price: Double) {
                            _totalPrice.value = _totalPrice.value!! - price
                        }

                        override fun onAccessItem(id: Int) {
                            startActivity(
                                DetailActivity.newIntent(this@CartActivity, id)
                            )
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }

//                        override fun onItemCheckedStatusChanged() {
//                            binding.cbSelectAll.isChecked = rvAdapter?.areAllItemsChecked() == true
//                        }
                    },
                    onSelectionChanged = { allChecked -> binding.cbSelectAll.isChecked = allChecked }
                )

                val rvLayoutManager = LinearLayoutManager(this@CartActivity)

                adapter = rvAdapter
                layoutManager = rvLayoutManager
            }

        })

        //Bagian penulisan code reaksi CheckBox
        binding.cbSelectAll.apply {
            setOnCheckedChangeListener {_, isChecked ->
                rvAdapter!!.setSelectAll(isChecked)
                if(isChecked){
                    rvAdapter!!.checkAllItems(true)
                }else{
                    rvAdapter!!.checkAllItems(false)
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
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

    private fun setLayoutForDialog(isShown: Boolean){
        if(isShown){
            binding.loadingLayout.visibility = View.VISIBLE
            binding.pbLoading.visibility = View.GONE
        }else{
            binding.loadingLayout.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE
        }
    }
}