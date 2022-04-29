package com.keystone.agencybankingapp.ui.domain.interfaces

import androidx.annotation.LayoutRes
import com.keystone.agencybankingapp.ui.data.entity.SavedBeneficiary
import com.keystone.agencybankingapp.ui.domain.adapters.ClickedData
import com.keystone.agencybankingapp.ui.domain.adapters.GenericRecyclerViewAdapter
import dagger.assisted.AssistedFactory

interface GenericRecyclerViewAdapterParameterFactory {
    fun <T> create(
        @LayoutRes layoutID: Int,
        bindingInterface: GenericSimpleRecyclerBindingInterface<T>,
        onClick: ClickedData<T>
    ): GenericRecyclerViewAdapter<ArrayList<SavedBeneficiary>>
}