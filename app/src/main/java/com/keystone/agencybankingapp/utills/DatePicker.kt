package com.keystone.agencybankingapp.utills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import com.afollestad.date.DatePicker
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentDatePickerBinding
import java.util.*

class DatePicker : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDatePickerBinding
    private lateinit var cancel: ImageView
    private lateinit var datePicker: DatePicker
    private lateinit var submitButton: ImageView
    private lateinit var selectedDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_date_picker, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()

        Calendar.getInstance().apply {

            val dayOfWeek = if (this.dayOfMonth < 9) {
                "0${this.dayOfMonth}"
            } else { "${this.dayOfMonth}" }

            val monthOfTheYear = if (this.month < 9) {
                "0${this.month+1}"
            } else { "${this.month+1}" }
            selectedDate = "${this.year}-$monthOfTheYear-$dayOfWeek"
        }
    }

    override fun onResume() {
        super.onResume()
        cancel.setOnClickListener {
            dismiss()
        }

        datePicker.onDateChanged {
            val dayOfWeek = if (it.dayOfMonth < 9) {
                "0${it.dayOfMonth}"
            } else { "${it.dayOfMonth}" }

            val monthOfTheYear = if (it.month < 9) {
                "0${it.month+1}"
            } else { "${it.month+1}" }
            selectedDate = "${it.year}-$monthOfTheYear-$dayOfWeek"
        }

        submitButton.setOnClickListener {
            setFragmentResult("selectedDate", bundleOf("dateOfBirth" to selectedDate))
            dismiss()
        }
    }

    private fun initializeViews() {
        cancel = binding.dismiss
        datePicker = binding.datePicker
        submitButton = binding.submit
    }

}