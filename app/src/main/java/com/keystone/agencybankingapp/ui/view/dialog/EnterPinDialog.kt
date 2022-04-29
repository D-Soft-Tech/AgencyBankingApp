package com.keystone.agencybankingapp.ui.view.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentEnterPinDialogBinding
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.AGENT_PIN_BUNDLE_KEY
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.AGENT_PIN_REQUEST_KEY
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.LOADING_DIALOG_FRAGMENT_TAG
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.delayOnLifecycle
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.hideKeyboard
import com.keystone.agencybankingapp.utills.LoadingDialog
import com.mukesh.OtpView
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnterPinDialog @Inject constructor() : DialogFragment() {
    private lateinit var binding: FragmentEnterPinDialogBinding
    private lateinit var otpView: OtpView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_enter_pin_dialog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.apply {
            setBackgroundDrawableResource(R.drawable.layout_page_with_curved_bg)
        }

        otpView.setOtpCompletionListener {
            setFragmentResult(AGENT_PIN_REQUEST_KEY, bundleOf(AGENT_PIN_BUNDLE_KEY to it))
            otpView.text?.clear()
            hideKeyboard()
            dismiss()
        }
    }

    private fun initViews() {
        otpView = binding.otpView
    }
}