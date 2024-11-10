package com.shoptastic.client.ui.activity.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shoptastic.client.R
import com.shoptastic.client.data.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.shoptastic.client.data.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.shoptastic.client.data.Constants.PREFERENCES.Companion.USERNAME_KEY
import com.shoptastic.client.data.model.other.ItemDropdown
import com.shoptastic.client.data.model.response.products.ProductResponse
import com.shoptastic.client.databinding.ActivityDashboardBinding
import com.shoptastic.client.databinding.DashboardBottomsheetLayoutBinding
import com.shoptastic.client.ui.activity.detail.DetailActivity
import com.shoptastic.client.ui.activity.login.LoginActivity
import com.shoptastic.client.ui.custom_components.InputDropdownView
import com.shoptastic.client.ui.custom_components.PopUpNotificationListener
import com.shoptastic.client.ui.custom_components.PopUpQuestionListener
import com.shoptastic.client.ui.custom_components.showPopUpNotification
import com.shoptastic.client.ui.custom_components.showPopUpQuestion
import com.shoptastic.client.ui.rv_adapters.ItemProductAdapter
import com.shoptastic.client.ui.viewmodels.dashboard.DashboardViewModel
import com.shoptastic.client.utils.Extension.Companion.retrieveListItemDropdownStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.log

class DashboardActivity : AppCompatActivity() {
    private val TAG = DashboardActivity::class.java.simpleName
    private lateinit var binding: ActivityDashboardBinding
    
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var rvAdapter: ItemProductAdapter?= null
    private lateinit var appPreferences: SharedPreferences

    companion object{
        fun newIntent(context: Context):Intent = Intent(context, DashboardActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appPreferences = this@DashboardActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        
        observeStatus()
        
        setUpView()
    }
    
    private fun observeStatus(){
        dashboardViewModel.isLoading.observe(this@DashboardActivity, {
            setLayoutForLoading(it)
        })
        
        dashboardViewModel.isFail.observe(this@DashboardActivity, {
            if(it){
                setLayoutForDialog(true)
                showPopUpNotification(
                    textTitle = getString(R.string.dialogTxtRetrievingFailedTitle),
                    textDesc = getString(R.string.dialogTxtRetrievingFailedDesc),
                    backgroundImage = R.drawable.ic_fail,
                    listener = object: PopUpNotificationListener{
                        override fun onPopUpClosed() {
                            setLayoutForDialog(false)
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
                            startActivity(DetailActivity.newIntent(this@DashboardActivity, item.id))
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

        binding.ivMenu.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this@DashboardActivity)

            val bottomSheetBinding = DashboardBottomsheetLayoutBinding.inflate(layoutInflater)

            bottomSheetDialog.setContentView(bottomSheetBinding.root)

            val retrievedUsername = appPreferences.getString(USERNAME_KEY, null)

            bottomSheetBinding.tvUsernameValue.text = retrievedUsername

            bottomSheetBinding.btnLogout.setOnClickListener {
                bottomSheetDialog.dismiss()
                setLayoutForDialog(true)
                this@DashboardActivity.showPopUpQuestion(
                    textTitle = getString(R.string.dialogTxtLogoutTitle),
                    textDesc = getString(R.string.dialogTxtLogoutDesc),
                    textBtnPositive = getString(R.string.btnTxtYes),
                    textBtnNegative = getString(R.string.btnTxtCancel),
                    listener = object: PopUpQuestionListener{
                        override fun onPostiveClicked() {
                            Log.d(TAG, "Logout")
                            closeOptionsMenu()
                            setLayoutForLoading(true)

                            lifecycleScope.launch(Dispatchers.IO) {
                                val editor = appPreferences.edit()

                                editor.remove(TOKEN_KEY)
                                editor.remove(USERNAME_KEY)

                                editor.apply()

                                withContext(Dispatchers.Main){
                                    setLayoutForDialog(true)
                                    this@DashboardActivity.showPopUpNotification(
                                        textTitle = getString(R.string.dialogTxtSuccessLogoutTitle),
                                        textDesc = getString(R.string.dialogTxtSuccessLogoutDesc),
                                        backgroundImage = R.drawable.ic_success,
                                        listener = object: PopUpNotificationListener{
                                            override fun onPopUpClosed() {
                                                startActivity(LoginActivity.newIntent(this@DashboardActivity))
                                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                                finish()
                                            }
                                        }
                                    )
                                }
                            }

                        }

                        override fun onNegativeClicked() {
                            closeOptionsMenu()
                            setLayoutForDialog(false)
                        }

                    }
                )
            }

            bottomSheetDialog.show()
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