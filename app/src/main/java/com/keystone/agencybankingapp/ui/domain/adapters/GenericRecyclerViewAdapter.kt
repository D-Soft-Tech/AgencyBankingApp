package com.keystone.agencybankingapp.ui.domain.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keystone.agencybankingapp.ui.domain.interfaces.GenericSimpleRecyclerBindingInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

typealias ClickedData<T> = (clickedData: T) ->Unit

class GenericRecyclerViewAdapter<T> @AssistedInject constructor (
    @Assisted @LayoutRes val layoutID: Int,
    @Assisted private val bindingInterface: GenericSimpleRecyclerBindingInterface<T>,
    @Assisted private val onClick: ClickedData<T>
) : RecyclerView.Adapter<GenericRecyclerViewAdapter.ViewHolder>() {

    private var dataSet: ArrayList<T> = arrayListOf()

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun <T> bind(
            item: T,
            bindingInterface: GenericSimpleRecyclerBindingInterface<T>
        ) = bindingInterface.bindData(item, view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(layoutID, parent, false))

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bind(dataSet[position], bindingInterface)
            itemView.setOnClickListener {
                onClick(dataSet[position])
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: ArrayList<T>) {
        val diff = GenericRvDiffUtil(data, dataSet)
        val diffResult = DiffUtil.calculateDiff(diff)
        dataSet = data
        notifyDataSetChanged()
        diffResult.dispatchUpdatesTo(this)
    }
}