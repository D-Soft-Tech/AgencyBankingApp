package com.keystone.agencybankingapp.ui.view.main.eNaira

import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentENairaHomePageBinding

class ENairaHomePage : Fragment() {
    private lateinit var binding: FragmentENairaHomePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_e_naira_home_page, container, false)
        return binding.root
    }
}