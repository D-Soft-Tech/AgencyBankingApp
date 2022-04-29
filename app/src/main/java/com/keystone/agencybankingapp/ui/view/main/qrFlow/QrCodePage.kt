package com.keystone.agencybankingapp.ui.view.main.qrFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentQrCodePageBinding

class QrCodePage : Fragment() {
    private lateinit var binding: FragmentQrCodePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qr_code_page, container, false)
        return binding.root
    }
}