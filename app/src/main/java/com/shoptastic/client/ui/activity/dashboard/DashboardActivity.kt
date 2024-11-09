package com.shoptastic.client.ui.activity.dashboard

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.shoptastic.client.R
import com.shoptastic.client.data.model.other.ItemDropdown
import com.shoptastic.client.data.model.response.products.ProductResponse
import com.shoptastic.client.databinding.ActivityDashboardBinding
import com.shoptastic.client.ui.custom_components.InputDropdownView
import com.shoptastic.client.ui.custom_components.PopUpNotificationListener
import com.shoptastic.client.ui.custom_components.showPopUpNotification
import com.shoptastic.client.ui.rv_adapters.ItemProductAdapter
import com.shoptastic.client.ui.viewmodels.dashboard.DashboardViewModel
import com.shoptastic.client.utils.Extension.Companion.retrieveListItemDropdownStatus
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.log

class DashboardActivity : AppCompatActivity() {
    private val TAG = DashboardActivity::class.java.simpleName
    private lateinit var binding: ActivityDashboardBinding
    
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var rvAdapter: ItemProductAdapter?= null

    companion object{
        fun newIntent(context: Context):Intent = Intent(context, DashboardActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        observeStatus()
        
        setUpView()
    }
    
    private fun observeStatus(){
        dashboardViewModel.isLoading.observe(this@DashboardActivity, {
            setLayoutForLoading(it)
        })
        
        dashboardViewModel.isFail.observe(this@DashboardActivity, {
            if(it){
                setLayoutForPopUp(true)
                showPopUpNotification(
                    textTitle = getString(R.string.dialogTxtRetrievingFailedTitle),
                    textDesc = getString(R.string.dialogTxtRetrievingFailedDesc),
                    backgroundImage = R.drawable.ic_fail,
                    listener = object: PopUpNotificationListener{
                        override fun onPopUpClosed() {
                            setLayoutForPopUp(false)
                        }
                    }
                )
            }
        })
    }

    private fun setUpView(){
        dashboardViewModel.getCategories()
        dashboardViewModel.getProducts()

        dashboardViewModel.categoriesResponse.observe(this@DashboardActivity, {categories->
            val listOption = retrieveListItemDropdownStatus(categories)

            binding.idvCategory.apply {
                setTitleInvisible()
                setHint(getString(R.string.dropdownCategories))
                setData(listOption)
                setListener(object: InputDropdownView.DropdownListener{

                    override fun onItemSelected(
                        position: Int,
                        item: String,
                        selectedData: ItemDropdown
                    ) {
                        setText(item)
                        val selectedCategories = item
                        Log.d(TAG, "Selected Categories: $selectedCategories")
                        dashboardViewModel.getProductsByCategory(item)
                    }
                })
            }
        })

        dashboardViewModel.productResponse.observe(this@DashboardActivity, {response->
            binding.rvProducts.apply {
                rvAdapter = ItemProductAdapter(
                    response.toMutableList(),
                    object: ItemProductAdapter.ItemListener{
                        override fun onClick(item: ProductResponse) {
                            Log.d(TAG, "product: $item")
                        }
                    }
                )

                val rvLayoutManager = GridLayoutManager(this@DashboardActivity, 2)

                adapter = rvAdapter
                layoutManager = rvLayoutManager
            }
        })

        dashboardViewModel.productsByCategoriesResponse.observe(this@DashboardActivity, {response->
            rvAdapter!!.updateItem(response)
        })
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