package com.keystone.agencybankingapp.ui.view.main.fundwalletflow

import android.os.Bundle
import android.os.CancellationSignal
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentOtpPageBinding
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants
import com.mukesh.OtpView
import com.mukesh.OnOtpCompletionListener
import kotlinx.coroutines.launch


class OtpPage : Fragment() {
    private lateinit var binding: FragmentOtpPageBinding
    private lateinit var otpView: OtpView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp_page, container, false)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragment?.view?.findViewById<ViewPager2>(R.id.viewPager)?.currentItem = 0
            }

        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        otpView.setOtpCompletionListener {
            lifecycleScope.launch {
                Toast.makeText(requireContext(), getString(R.string.completed), Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun initViews() {
        otpView = binding.otpView
    }
}