package com.keystone.agencybankingapp.ui.view.main.fundwalletflow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentFundWalletViewPagerPageBinding
import com.keystone.agencybankingapp.ui.di.fundWallet.FundWalletScreens
import com.keystone.agencybankingapp.ui.di.fundWallet.FundWalletScreensImplementationClass
import com.keystone.agencybankingapp.ui.domain.adapters.AppViewPagerAdapters
import com.keystone.agencybankingapp.ui.domain.adapters.AppViewPagerAdaptersFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FundWalletViewPagerPage : Fragment() {
    private lateinit var binding: FragmentFundWalletViewPagerPageBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var screens: ArrayList<Fragment>

    private lateinit var myAdapter: AppViewPagerAdapters
    @Inject
    lateinit var appViewPagerFactory: AppViewPagerAdaptersFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_fund_wallet_view_pager_page,
            container,
            false
        )

        myAdapter = appViewPagerFactory.createAppViewPagerAdapter(childFragmentManager, lifecycle)

        screens = arrayListOf(
            FundWalletFirstPage(),
            AddCard(),
            OtpPage()
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

        myAdapter.setScreens(screens)
        viewPager.adapter = myAdapter
    }

    private fun initViews() {
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