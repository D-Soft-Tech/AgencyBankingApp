package com.keystone.agencybankingapp.ui.view.main.openAccountScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentOpenAccountAddressScreenBinding

class OpenAccountAddressScreen : Fragment() {
    private lateinit var binding: FragmentOpenAccountAddressScreenBinding
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var selectBranch: AutoCompleteTextView
    private lateinit var previousBtn: Button
    private lateinit var nextBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_open_account_address_screen,
            container,
            false
        )
        arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.dormieBranch)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        selectBranch.setAdapter(arrayAdapter)
    }

    override fun onResume() {
        super.onResume()
        previousBtn.setOnClickListener {
            (requireParentFragment() as NewAccountOpeningViewPager).gotoPreviousStep()
        }

        nextBtn.setOnClickListener {
            (requireParentFragment() as NewAccountOpeningViewPager).gotoNextStep()
        }
    }

    private fun initializeViews() {
        selectBranch = binding.branch
        previousBtn = binding.previousPage
        nextBtn = binding.proceedBtn
    }
}