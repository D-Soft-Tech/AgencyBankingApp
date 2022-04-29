package com.keystone.agencybankingapp.ui.domain.adapters

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import dagger.assisted.AssistedFactory

@AssistedFactory
interface AppViewPagerAdaptersFactory {
    fun createAppViewPagerAdapter(
        fm: FragmentManager,
        lifecycle: Lifecycle
    ): AppViewPagerAdapters
}