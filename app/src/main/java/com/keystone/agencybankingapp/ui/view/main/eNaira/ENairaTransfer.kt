package com.keystone.agencybankingapp.ui.view.main.eNaira

import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentENairaTransferBinding
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.runFingerPrintScanner

class ENairaTransfer : Fragment() {
    private lateinit var binding: FragmentENairaTransferBinding
    private lateinit var fingerPrintScanner: ImageView
    private var cancellationSignal: CancellationSignal? = null

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.fingerPrintAuthDeclined),
                Toast.LENGTH_SHORT
            ).show()
            AppConstants.notifyUserWithSnackBar(
                getString(R.string.fingerPrintAuthDeclined),
                requireContext(),
                binding.root
            )
        }
        return cancellationSignal as CancellationSignal
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_e_naira_transfer, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onResume() {
        super.onResume()
        fingerPrintScanner.setOnClickListener {
            runFingerPrintScanner(
                requireContext(),
                binding.root,
                requireActivity(),
                getCancellationSignal()
            ) {
                Toast.makeText(requireContext(), getString(R.string.completed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {
        fingerPrintScanner = binding.imageView3
    }
}