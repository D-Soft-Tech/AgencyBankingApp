package com.keystone.agencybankingapp.ui.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentBettingAndLotteryBinding

class BettingAndLottery : Fragment() {
    private lateinit var binding: FragmentBettingAndLotteryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_betting_and_lottery, container, false)
        return binding.root
    }
}