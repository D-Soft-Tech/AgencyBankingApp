package com.keystone.agencybankingapp.ui.view.main

import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentFundsTransferBinding
import com.keystone.agencybankingapp.ui.data.entity.SavedBeneficiary
import com.keystone.agencybankingapp.ui.domain.adapters.GenericRecyclerViewAdapter
import com.keystone.agencybankingapp.ui.domain.interfaces.GenericSimpleRecyclerBindingInterface
import com.keystone.agencybankingapp.ui.view.dialog.EnterPinDialog
import com.keystone.agencybankingapp.ui.view.dialog.SavedBeneficiaryDialogFragment
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.AGENT_PIN_BUNDLE_KEY
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.AGENT_PIN_DIALOG_TAG
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.AGENT_PIN_REQUEST_KEY
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.LOADING_DIALOG_FRAGMENT_TAG
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.SAVED_BENEFICIARY_BOTTOM_SHEET_TAG
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.SAVED_BENEFICIARY_CLICKED_ITEM_BUNDLE_KEY
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.SAVED_BENEFICIARY_CLICKED_ITEM_REQUEST_KEY
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.notifyUserWithSnackBar
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.runFingerPrintScanner
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.validateInputFieldsNotEmpty
import com.keystone.agencybankingapp.utills.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FundsTransfer : Fragment() {
    private lateinit var binding: FragmentFundsTransferBinding
    private var selectedBeneficiary: SavedBeneficiary? = null
    private lateinit var chooseSavedBeneficiaryTv: TextView
    private lateinit var selectBankOfBeneficiary: AutoCompleteTextView
    private lateinit var beneficiaryAccountNumber: TextInputEditText
    private lateinit var beneficiaryAccountName: TextInputEditText
    private lateinit var amountToTransfer: TextInputEditText
    private lateinit var transferNarration: TextInputEditText
    private lateinit var completeTransferBtn: Button
    private var cancellationSignal: CancellationSignal? = null
    private lateinit var fingerPrintImage: ImageView

    @Inject
    lateinit var otpDialog: EnterPinDialog

    @Inject
    lateinit var loadingDialog: LoadingDialog

    @Inject
    lateinit var savedBeneficiaryBottomDialog: SavedBeneficiaryDialogFragment

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.fingerPrintAuthDeclined),
                Toast.LENGTH_SHORT
            ).show()
            notifyUserWithSnackBar(
                getString(R.string.fingerPrintAuthDeclined),
                requireContext(),
                binding.root
            )
        }
        return cancellationSignal as CancellationSignal
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(SAVED_BENEFICIARY_CLICKED_ITEM_REQUEST_KEY) { _, bundle ->
            selectedBeneficiary = bundle.getParcelable(SAVED_BENEFICIARY_CLICKED_ITEM_BUNDLE_KEY)
            selectedBeneficiary?.run {
                selectBankOfBeneficiary.setText(bankName)
                beneficiaryAccountNumber.setText(accountNumber)
                beneficiaryAccountName.setText(accountName.uppercase())
            }
        }

        setFragmentResultListener(AGENT_PIN_REQUEST_KEY) { _, bundle ->
            val agentPin = bundle.getString(AGENT_PIN_BUNDLE_KEY)

            // MAKE NETWORK CALL
            // ==== NETWORK CALL === //
            loadingDialog.show(
                parentFragmentManager,
                LOADING_DIALOG_FRAGMENT_TAG
            ) // Show progressbar while making the network call

            lifecycleScope.launch(Dispatchers.Main) {
                delay(1000)
                loadingDialog.dismiss()

                Snackbar.make(
                    requireContext(),
                    binding.root,
                    agentPin.toString(),
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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_funds_transfer, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onResume() {
        super.onResume()
        fingerPrintImage.setOnClickListener {
            runFingerPrintScanner(
                requireContext(),
                binding.root,
                requireActivity(),
                getCancellationSignal()
            ) {
                dummyFunction()
            }
        }

        chooseSavedBeneficiaryTv.setOnClickListener {
            savedBeneficiaryBottomDialog.show(
                parentFragmentManager,
                SAVED_BENEFICIARY_BOTTOM_SHEET_TAG
            )
        }

        completeTransferBtn.setOnClickListener {
            validateInputFieldsNotEmpty(
                requireContext(),
                binding.root,
                beneficiaryAccountNumber,
                beneficiaryAccountName,
                amountToTransfer,
                transferNarration
            ) {
                otpDialog.show(parentFragmentManager, AGENT_PIN_DIALOG_TAG)
            }
        }
    }

    private fun dummyFunction() {
        Toast.makeText(requireContext(), "Finger Print scanner is working", Toast.LENGTH_SHORT)
            .show()
    }

    private fun initViews() {

        with(binding) {
            fingerPrintImage = imageView3
            chooseSavedBeneficiaryTv = textView13
            selectBankOfBeneficiary = selectBank
            beneficiaryAccountNumber = accountNumber
            amountToTransfer = amount
            transferNarration = narration
            beneficiaryAccountName = accountNameOfBeneficiary
            completeTransferBtn = proceedBtn
        }
    }
}