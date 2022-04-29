package com.keystone.agencybankingapp.ui.domain.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Singleton

class AppViewPagerAdapters @AssistedInject constructor(
    @Assisted fm: FragmentManager,
    @Assisted lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {
    private val screens: ArrayList<Fragment> = arrayListOf()
    override fun getItemCount() = screens.size

    override fun createFragment(position: Int) = screens[position]

    fun setScreens(screensToShow: ArrayList<Fragment>) {
        screens.addAll(screensToShow)
    }

    fun getFragment(position: Int) = screens[position]
}