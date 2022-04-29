package com.keystone.agencybankingapp.ui.view.main.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentDashboardBinding
import com.keystone.agencybankingapp.ui.view.main.MainActivity
import com.keystone.agencybankingapp.ui.view.main.interfaces.DrawerLocker

class Dashboard : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var menuToggle: ImageView
    private lateinit var welcomeTextView: TextView
    private lateinit var inboxImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        (activity as DrawerLocker).setDrawerEnabled(true)
        menuToggle.setOnClickListener {
            (activity as MainActivity).findViewById<DrawerLayout>(R.id.drawerLayout)
                .openDrawer(GravityCompat.START)
        }
    }

    private fun initViews() {
        menuToggle = binding.toggle
        welcomeTextView = binding.welcomeText
        inboxImageView = binding.inbox
    }
}