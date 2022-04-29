package com.keystone.agencybankingapp.ui.view.main.qrFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentQrPaymentPageBinding
import com.keystone.agencybankingapp.ui.domain.adapters.AppViewPagerAdapters
import com.keystone.agencybankingapp.ui.view.main.fundwalletflow.AddCard
import com.keystone.agencybankingapp.ui.view.main.fundwalletflow.FundWalletFirstPage
import com.keystone.agencybankingapp.ui.view.main.fundwalletflow.OtpPage

class QrPaymentViewPagerPage : Fragment() {
    private lateinit var binding: FragmentQrPaymentPageBinding
    private lateinit var generateQrCodeBtn: Button
    private lateinit var myAdapter: AppViewPagerAdapters
    private lateinit var viewPager: ViewPager2
    private lateinit var screens: ArrayList<Fragment>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_qr_payment_page, container, false)

        myAdapter = AppViewPagerAdapters(childFragmentManager, lifecycle)

        screens = arrayListOf(
            QrTransactionSelectionPage(),
            QrCodePage()
        )

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewPager.currentItem > 0) {
                    gotoPrevious()
                } else {
                    findNavController().popBackStack()
                }
            }

        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 1) {
                    generateQrCodeBtn.visibility = View.INVISIBLE
                } else {
                    generateQrCodeBtn.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        myAdapter.setScreens(screens)
        viewPager.adapter = myAdapter

        generateQrCodeBtn.setOnClickListener {
            viewPager.currentItem += 1
        }
    }

    private fun initViews() {
        generateQrCodeBtn = binding.proceedBtn
        viewPager = binding.viewPager.also {
            it.isUserInputEnabled = false
        }
    }

    fun gotoPrevious() {
        viewPager.currentItem -= 1
    }

    fun gotoNext() {
        viewPager.currentItem += 1
    }
}