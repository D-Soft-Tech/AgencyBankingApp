package com.keystone.agencybankingapp.ui.domain.interfaces

import android.view.View

interface GenericSimpleRecyclerBindingInterface<T> {
    fun bindData(item: T, view: View)
}