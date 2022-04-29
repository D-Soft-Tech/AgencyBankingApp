package com.keystone.agencybankingapp.ui.view.loginFlows

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentLoginBinding
import com.keystone.agencybankingapp.ui.view.main.MainActivity
import com.paxsz.demo.emvdemo.PaxDevice

class LoginScreen : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        activity?.window?.apply {
            statusBarColor = resources.getColor(R.color.keyStoneColor)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        loginBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun initViews() {
        loginBtn = binding.proceedBtn
    }
}