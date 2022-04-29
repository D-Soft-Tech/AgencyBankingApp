package com.keystone.agencybankingapp.ui.view.main.withdrawal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentWithdrawalBinding
import com.keystone.agencybankingapp.ui.domain.adapters.AppViewPagerAdapters
import com.keystone.agencybankingapp.ui.domain.adapters.AppViewPagerAdaptersFactory
import com.keystone.agencybankingapp.ui.view.dialog.EnterPinDialog
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.AGENT_PIN_BUNDLE_KEY
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.AGENT_PIN_REQUEST_KEY
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.LOADING_DIALOG_FRAGMENT_TAG
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.validateInputFieldsNotEmpty
import com.keystone.agencybankingapp.utills.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WithdrawalViewPagerPager @Inject constructor() : Fragment() {
    private lateinit var binding: FragmentWithdrawalBinding
    private lateinit var proceedBtnView: Button
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: AppViewPagerAdapters

    @Inject
    lateinit var viewPagerAdapterFactory: AppViewPagerAdaptersFactory

    @Inject
    lateinit var enterPinDialog: EnterPinDialog

    @Inject
    lateinit var loadingDialog: LoadingDialog

    private lateinit var viewPagerScreens: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(AGENT_PIN_REQUEST_KEY) { _, bundle ->
            // After the Pin has ben submitted to this fragment
            loadingDialog.show(
                parentFragmentManager,
                LOADING_DIALOG_FRAGMENT_TAG
            ) // Make the call with the pin and show progress dialog while doing this
            lifecycleScope.launch {
                delay(1000)
                loadingDialog.dismiss()

                Snackbar.make(
                    requireContext(),
                    binding.root,
                    bundle.getString(AGENT_PIN_BUNDLE_KEY).toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                delay(1500)
                findNavController().popBackStack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_withdrawal, container, false)

        viewPagerScreens = arrayListOf(
            WithdrawalInputPage(),
            WithdrawalAuthenticationPage()
        )

        // Instantiate the Adapter
        viewPagerAdapter =
            viewPagerAdapterFactory.createAppViewPagerAdapter(parentFragmentManager, lifecycle)

        // Set Screens tp Adapter
        viewPagerAdapter.setScreens(viewPagerScreens)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        viewPager.apply {
            isUserInputEnabled = false
            adapter = viewPagerAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        proceedBtnView.setOnClickListener {
            if (viewPager.currentItem == 0) {
                val viewOfTheFragment = viewPagerAdapter.getFragment(0).view
                val withdrawalAccountNumber =
                    viewOfTheFragment?.findViewById<TextInputEditText>(R.id.withdrawalAccountNumber)

                val withdrawalAmount =
                    viewOfTheFragment?.findViewById<TextInputEditText>(R.id.withdrawalAmount)

                if (withdrawalAccountNumber != null && withdrawalAmount != null) {
                    validateInputFieldsNotEmpty(
                        requireContext(),
                        binding.root,
                        withdrawalAccountNumber,
                        withdrawalAmount
                    ) {
                        viewPager.currentItem += 1
                    }
                }
            } else if (viewPager.currentItem == 1) {
                val viewOfTheFragment = viewPagerAdapter.getFragment(1).view
                val otpEditTextView =
                    viewOfTheFragment?.findViewById<TextInputEditText>(R.id.otp)

                val customerPinEditTextView =
                    viewOfTheFragment?.findViewById<TextInputEditText>(R.id.customerPin)

                if (otpEditTextView != null && customerPinEditTextView != null) {
                    validateInputFieldsNotEmpty(
                        requireContext(),
                        binding.root,
                        otpEditTextView,
                        customerPinEditTextView
                    ) {
                        enterPinDialog.show(parentFragmentManager, LOADING_DIALOG_FRAGMENT_TAG)
                    }
                }
            }
        }
    }

    private fun initViews() {
        with(binding) {
            viewPager = viewPager2
            proceedBtnView = proceedBtn
        }
    }
}