package com.keystone.agencybankingapp.ui.domain.adapters

import androidx.recyclerview.widget.DiffUtil

class GenericRvDiffUtil<T>(
    private val newList: ArrayList<T>,
    private val oldList: ArrayList<T>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        newList[newItemPosition] == oldList[oldItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        newList[newItemPosition] == oldList[oldItemPosition]
}