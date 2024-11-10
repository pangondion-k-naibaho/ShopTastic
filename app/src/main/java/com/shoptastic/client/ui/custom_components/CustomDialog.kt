package com.shoptastic.client.ui.custom_components

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.view.Window
import com.bumptech.glide.Glide
import com.shoptastic.client.R
import com.shoptastic.client.databinding.PopupNotificationLayoutBinding
import com.shoptastic.client.databinding.PopupQuestionLayoutBinding

interface PopUpNotificationListener{
    fun onPopUpClosed()
}

interface PopUpQuestionListener{
    fun onPostiveClicked()

    fun onNegativeClicked()
}

fun Activity.showPopUpNotification(
    textTitle: String,
    textDesc: String,
    backgroundImage: Int,
    popUpDuration: Long?=3000L,
    listener: PopUpNotificationListener?=null
){
    val dialog = Dialog(this)
    val binding = PopupNotificationLayoutBinding.bind(layoutInflater.inflate(R.layout.popup_notification_layout, null))
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(binding.root)
    dialog.setCancelable(false)
    binding.apply {
        tvPopUpTitle.text = textTitle
        tvPopUpDesc.text = textDesc
        Glide.with(this@showPopUpNotification)
            .load(backgroundImage)
            .into(ivPopUp)

        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
                listener?.onPopUpClosed()
            }
        }, popUpDuration!!)
        if(!isFinishing) dialog.show()

    }
}

fun Activity.showPopUpQuestion(
    textTitle: String,
    textDesc: String,
    textBtnPositive: String,
    textBtnNegative: String,
    listener: PopUpQuestionListener
){
    val dialog = Dialog(this)
    val binding = PopupQuestionLayoutBinding.bind(layoutInflater.inflate(R.layout.popup_question_layout, null))

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(binding.root)
    dialog.setCancelable(false)
    binding.apply {
        tvPopUpTitle.text = textTitle
        tvPopUpDesc.text = textDesc
        btnPositive.apply {
            text = textBtnPositive
            setOnClickListener {
                dialog.dismiss()
                listener.onPostiveClicked()
            }
        }
        btnNegative.apply {
            text = textBtnNegative
            setOnClickListener {
                dialog.dismiss()
                listener.onNegativeClicked()
            }
        }
        if(!isFinishing) dialog.show()
    }
}