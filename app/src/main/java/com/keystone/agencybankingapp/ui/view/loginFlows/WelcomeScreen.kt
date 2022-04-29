package com.keystone.agencybankingapp.ui.view.loginFlows

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentSecondSplashScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WelcomeScreen : Fragment() {
    private lateinit var binding: FragmentSecondSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_second_splash_screen,
            container,
            false
        )

        activity?.window?.apply {
            statusBarColor = resources.getColor(R.color.white)
        }

        lifecycleScope.launch(Dispatchers.Main) {
            delay(4000)
            findNavController().navigate(R.id.action_welcomeScreen_to_loginScreen)
        }

        return binding.root
    }
}