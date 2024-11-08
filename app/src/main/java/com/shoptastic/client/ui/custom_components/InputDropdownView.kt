package com.shoptastic.client.ui.custom_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.shoptastic.client.R
import com.shoptastic.client.data.model.other.ItemDropdown
import com.shoptastic.client.databinding.DropdownLayoutBinding
import com.shoptastic.client.databinding.DropdownPopupLayoutBinding
import com.shoptastic.client.ui.rv_adapters.ItemDropdownAdapter

class InputDropdownView: ConstraintLayout {
    private lateinit var mContext: Context
    private lateinit var binding: DropdownLayoutBinding

    private var dropdownListener: DropdownListener?= null
    private var listDropdown: List<ItemDropdown> = ArrayList()
    private lateinit var popupBinding: DropdownPopupLayoutBinding
    private lateinit var popUpWindow: PopupWindow
    private var isDropdownShown: Boolean = false

    private lateinit var itemDropdownAdapter: ItemDropdownAdapter
    private var itemListener: ItemDropdownAdapter.ItemListener?= null

    constructor(context: Context): super(context){
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init(context, attrs)
    }

    private fun init(context: Context, attributeSet: AttributeSet?){
        mContext = context

        binding = DropdownLayoutBinding.bind(
            LayoutInflater.from(mContext)
                .inflate(R.layout.dropdown_layout, this, true)
        )

        binding.clDropdownContent.setOnClickListener {
            dropdownListener?.onDropdownClicked()
            if(listDropdown.size > 0){
                if(!popUpWindow.isShowing){
                    binding.spContent.performClick()
                    showPopUp()
                }else{
                    dismissPopUp()
                }
            }
        }

        binding.tvSelectedItem.setOnClickListener {
            dropdownListener?.onDropdownClicked()
            if(listDropdown.size > 0){
                if(!popUpWindow.isShowing){
                    binding.spContent.performClick()
                    showPopUp()
                }else{
                    dismissPopUp()
                }
            }
        }

        val popUpView = LayoutInflater.from(mContext).inflate(R.layout.dropdown_popup_layout, null)
        popupBinding = DropdownPopupLayoutBinding.bind(popUpView)

        popUpWindow = PopupWindow(
            popUpView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        popUpWindow.setOnDismissListener {
            dropdownListener?.onDismissPopUp()
        }
        itemDropdownAdapter = ItemDropdownAdapter(listDropdown!!, itemListener)
    }

    fun setData(list: List<ItemDropdown>){
        this.listDropdown = list
//        itemDropdownAdapter.updateChecked(list[0])
//        if(list.size>0)
        itemDropdownAdapter = ItemDropdownAdapter(listDropdown!!, object: ItemDropdownAdapter.ItemListener{
            override fun onClick(item: ItemDropdown, position: Int) {
                itemDropdownAdapter.updateChecked(item)
                dismissPopUp()
                dropdownListener?.onItemSelected(position, item.name!!, item)
            }
        })
        popupBinding.rvItem.apply {
            layoutManager = LinearLayoutManager(context.applicationContext)
            adapter = itemDropdownAdapter
            layoutParams.height = when(itemDropdownAdapter.data.size < 5){
                true -> ViewGroup.LayoutParams.WRAP_CONTENT
                false -> 400
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        popUpWindow.width = w
        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun setText(input: String){
        binding.tvSelectedItem.text = input
    }

    fun setHint(hint: String){
        binding.tvSelectedItem.text = hint
    }

    fun setListener(listener: DropdownListener){
        dropdownListener = listener
    }

//    fun setAdapter(adapter: ItemDropdownAdapter){
//        itemDropdownAdapter = adapter
//    }

    fun setNormal(){
        binding.clDropdownContent.background = ContextCompat.getDrawable(mContext, R.drawable.bg_circular_rectangle_electriceel_iwhite)
    }

    fun setTitle(title: String){
        binding.tvTitle.text = title
    }

    fun setTitleInvisible(){
        binding.tvTitle.visibility = View.GONE
    }

    private fun showPopUp(){
        popUpWindow.showAsDropDown(binding.root, 0, 10)
    }

    private fun dismissPopUp(){
        popUpWindow.dismiss()
    }

    interface DropdownListener{
        fun onDropdownClicked(){}

        fun onItemSelected(position: Int, item: String, selectedData: ItemDropdown){}

        fun onDismissPopUp(){}
    }
}