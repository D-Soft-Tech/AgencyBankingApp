package com.keystone.agencybankingapp.ui.view.main.fundwalletflow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentFundWalletFirstPageBinding

class FundWalletFirstPage : Fragment() {
    private lateinit var binding: FragmentFundWalletFirstPageBinding
    private lateinit var addCard: ConstraintLayout
    private lateinit var eNaira: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fund_wallet_first_page, container, false)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (requireParentFragment() as FundWalletViewPagerPage).gotoPrevious()
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
        addCard.setOnClickListener {
            (requireParentFragment() as FundWalletViewPagerPage).gotoNext()
        }

        eNaira.setOnClickListener {
            findNavController().navigate(R.id.action_fundWalletViewPagerPage_to_ENairaHomePage)
        }
    }

    private fun initViews() {
        addCard = binding.funWithCard
        eNaira = binding.fundWitheNaira
    }
}