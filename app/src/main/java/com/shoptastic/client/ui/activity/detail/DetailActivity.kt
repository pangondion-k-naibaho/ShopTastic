package com.shoptastic.client.ui.activity.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.shoptastic.client.R
import com.shoptastic.client.data.Constants
import com.shoptastic.client.data.model.other.ProductSaved
import com.shoptastic.client.data.model.response.products.ProductResponse
import com.shoptastic.client.databinding.ActivityDetailBinding
import com.shoptastic.client.ui.custom_components.PopUpNotificationListener
import com.shoptastic.client.ui.custom_components.showPopUpNotification
import com.shoptastic.client.ui.viewmodels.detail.DetailViewModel
import com.shoptastic.client.utils.Extension.Companion.generateDataId
import com.shoptastic.client.utils.Extension.Companion.toCurrencyString
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private val TAG = DetailActivity::class.java.simpleName
    private lateinit var binding: ActivityDetailBinding
    private var retrievedId: Int?= 0
    private val detailViewModel: DetailViewModel by viewModel()

    private var retrievedProduct: ProductResponse?= null

    companion object{
        const val EXTRA_ID = "EXTRA_ID"
        fun newIntent(context: Context, deliveredId: Int): Intent = Intent(context, DetailActivity::class.java)
            .putExtra(EXTRA_ID, deliveredId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrievedId = intent.extras!!.getInt(EXTRA_ID)

        observeStatus()

        setUpView()
    }

    private fun observeStatus(){
        detailViewModel.isLoading.observe(this@DetailActivity, {
            setLayoutForLoading(it)
        })

        detailViewModel.isFail.observe(this@DetailActivity, {
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

        detailViewModel.isSaveSuccess.observe(this@DetailActivity, {
            if(it) Toast.makeText(this@DetailActivity, getString(R.string.toastSaveToCartSuccess), Toast.LENGTH_SHORT).show()
        })
    }

    private fun setUpView(){
        setSupportActionBar(binding.detailActionbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setDisplayShowTitleEnabled(false)
        }

        detailViewModel.getDetailProduct(retrievedId.toString())

        detailViewModel.detailProductResponse.observe(this@DetailActivity, {response->
            retrievedProduct = response

            binding.apply {

                Glide.with(this@DetailActivity)
                    .load(response.image)
                    .into(ivProduct)

                tvProductPrice.text = response.price.toCurrencyString()

                tvProductName.text = response.title

                tvProductRating.text = "${response.rating.rate}"

                tvCountRater.text = String.format(getString(R.string.tvFormatCountRater), response.rating.count)

                tvProductDescription.text = response.description

                tvCategory.apply {
                    text = response.category

                    val color = when(response.category){
                        Constants.PRODUCT_CATEGORY.ELECTRONICS -> R.color.grenadine_pink
                        Constants.PRODUCT_CATEGORY.JEWELRY -> R.color.droplet
                        Constants.PRODUCT_CATEGORY.MENS_CLOTHING -> R.color.aqua_fiesta
                        Constants.PRODUCT_CATEGORY.WOMENS_CLOTHING -> R.color.dwarven_peaches
                        else -> R.color.black
                    }

                    setTextColor(ContextCompat.getColor(this@DetailActivity, color))
                }
            }
        })


        binding.apply {
            btnAddToCart.setOnClickListener {
                Log.d(TAG, "retrieved product: ${retrievedProduct}")
                val dataId = generateDataId()

                val productNeedToBeSaved = ProductSaved(dataId, retrievedProduct!!)

                detailViewModel.saveProduct(productNeedToBeSaved)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menuCart ->{
                Toast.makeText(this@DetailActivity, "Updated soon", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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