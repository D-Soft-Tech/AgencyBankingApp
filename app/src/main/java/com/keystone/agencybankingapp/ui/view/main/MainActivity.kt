package com.keystone.agencybankingapp.ui.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.aceinteract.android.stepper.StepperNavListener
import com.google.android.material.navigation.NavigationView
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.ActivityMainBinding
import com.keystone.agencybankingapp.ui.view.main.interfaces.DrawerLocker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor(): AppCompatActivity(), DrawerLocker, StepperNavListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AgencyBankingApp)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerLayout = binding.drawerLayout
        navView = binding.navView
    }

    override fun onResume() {
        super.onResume()
        handleNavigationWithMenu()
    }

    override fun setDrawerEnabled(isDrawerEnabled: Boolean) {
        val locker = if (isDrawerEnabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        drawerLayout.setDrawerLockMode(locker)
    }

    override fun onCompleted() {
        Toast.makeText(this, getString(R.string.completed), Toast.LENGTH_SHORT).show()
    }

    override fun onStepChanged(step: Int) {
        if (step == 3) {
            Toast.makeText(this, getString(R.string.completed), Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleNavigationWithMenu() {
        navView.setNavigationItemSelectedListener {
            when(it.itemId) {

                R.id.commission -> {
                    handleNavigation(R.id.action_dashboard_to_myCommissions2)
                    return@setNavigationItemSelectedListener true
                }

                R.id.loan -> {
                    handleNavigation(R.id.action_dashboard_to_loan2)
                    return@setNavigationItemSelectedListener true
                }

                R.id.my_accounts -> {
                    handleNavigation(R.id.action_dashboard_to_myAccounts)
                    return@setNavigationItemSelectedListener true
                }

                else -> {
                    closeDrawer()
                    return@setNavigationItemSelectedListener true
                }
            }
        }
    }

    private fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun handleNavigation(destinationId: Int) {
        closeDrawer()
        findNavController(R.id.fragment_container_view).navigate(destinationId)
    }
}