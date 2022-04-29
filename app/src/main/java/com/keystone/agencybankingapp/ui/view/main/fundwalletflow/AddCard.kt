package com.keystone.agencybankingapp.ui.view.main.fundwalletflow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentAddCardBinding

class AddCard : Fragment() {
    private lateinit var binding: FragmentAddCardBinding
    private lateinit var proceedButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_card, container, false)

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
        proceedButton.setOnClickListener {
            (requireParentFragment() as FundWalletViewPagerPage).gotoNext()
        }
    }

    private fun initViews() {
        proceedButton = binding.proceedBtn
    }
}