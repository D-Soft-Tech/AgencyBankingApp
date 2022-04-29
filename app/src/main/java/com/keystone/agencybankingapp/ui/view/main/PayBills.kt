package com.keystone.agencybankingapp.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentPayBillsBinding

class PayBills : Fragment() {
    private lateinit var binding: FragmentPayBillsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay_bills, container, false)
        return binding.root
    }
}