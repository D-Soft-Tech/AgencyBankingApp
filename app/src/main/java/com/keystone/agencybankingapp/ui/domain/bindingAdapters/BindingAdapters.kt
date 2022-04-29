package com.keystone.agencybankingapp.ui.domain.bindingAdapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("validate")
fun TextInputEditText.validateExpiryDate(str: Int) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        @SuppressLint("SetTextI18n")
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (start == 1 && start + count == 2 && s?.contains('/') == false) {
                this@validateExpiryDate.apply {
                    setText("$s/")
                    setSelection(this.length())
                }
            } else if (start == 3 && start - before == 2 && s?.contains('/') == true) {
                this@validateExpiryDate.apply {
                    setText(s.toString().replace("/", ""))
                    setSelection(this.length())
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    })
}

@BindingAdapter("navigation")
fun View.navigation(navId: Int) {
    setOnClickListener {
        it.findNavController().navigate(navId)
    }
}

@BindingAdapter("back")
fun ImageView.backNavigation(dummyInt: Int) {
    setOnClickListener {
        it.findNavController().popBackStack()
    }
}