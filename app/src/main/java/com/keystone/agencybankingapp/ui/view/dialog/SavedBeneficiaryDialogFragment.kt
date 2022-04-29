package com.keystone.agencybankingapp.ui.view.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentSavedBeneficiaryDialogBinding
import com.keystone.agencybankingapp.ui.data.entity.DummyData.getDummySavedBeneficiaries
import com.keystone.agencybankingapp.ui.data.entity.SavedBeneficiary
import com.keystone.agencybankingapp.ui.domain.adapters.GenericRecyclerViewAdapter
import com.keystone.agencybankingapp.ui.domain.interfaces.GenericSimpleRecyclerBindingInterface
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.SAVED_BENEFICIARY_CLICKED_ITEM_BUNDLE_KEY
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.SAVED_BENEFICIARY_CLICKED_ITEM_REQUEST_KEY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedBeneficiaryDialogFragment @Inject constructor(): BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSavedBeneficiaryDialogBinding
    private lateinit var cancelButton: ImageView
    private lateinit var searchView: SearchView
    private lateinit var dataForAdapter: ArrayList<SavedBeneficiary>
    private lateinit var recyclerView: RecyclerView
    private val bindingInterface: GenericSimpleRecyclerBindingInterface<SavedBeneficiary> =
        object : GenericSimpleRecyclerBindingInterface<SavedBeneficiary> {
            override fun bindData(item: SavedBeneficiary, view: View) {
                val accountNameTv = view.findViewById<TextView>(R.id.savedBeneficiaryAccountName)
                val bankNameTv = view.findViewById<TextView>(R.id.savedBeneficiaryBankName)
                val accountNumberTv =
                    view.findViewById<TextView>(R.id.savedBeneficiaryAccountNumber)
                with(item) {
                    accountNameTv.text = accountName.uppercase()
                    bankNameTv.text = bankName
                    accountNumberTv.text = accountNumber
                }
            }
        }


//    private lateinit var adapter: GenericRecyclerViewAdapter<ArrayList<SavedBeneficiary>>
//    @Inject
//    lateinit var inter: GenericRecyclerViewAdapterParameterFactory

    lateinit var myGenericRvAdapter: GenericRecyclerViewAdapter<SavedBeneficiary>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_saved_beneficiary_dialog,
            container,
            false
        )

        // Data to pass into the adapter
        dataForAdapter = getDummySavedBeneficiaries()

        myGenericRvAdapter = GenericRecyclerViewAdapter(
            R.layout.saved_beneficiary_item_layout,
            bindingInterface
        ) { selectedSavedBeneficiary ->
            handleSavedBeneficiaryItemOnClicked(selectedSavedBeneficiary)
        }
        myGenericRvAdapter.updateData(dataForAdapter)

//        adapter = inter.create(
//            R.layout.saved_beneficiary_item_layout,
//            bindingInterface
//        ) { selectedSavedBeneficiary ->
//            handleSavedBeneficiaryItemOnClicked(selectedSavedBeneficiary)
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        recyclerView.adapter = myGenericRvAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val placeHolderDataForAdapter = dataForAdapter.filter {
                    it.accountName.lowercase()
                        .contains(newText.toString().lowercase()) || it.accountNumber.contains(
                        newText.toString()
                    ) || it.bankName.lowercase().contains(newText.toString().lowercase())
                }
                myGenericRvAdapter.updateData(placeHolderDataForAdapter as ArrayList<SavedBeneficiary>)
                return true
            }

        })

        cancelButton.setOnClickListener {
            dismiss()
            Log.d("adapterData", myGenericRvAdapter.itemCount.toString())
        }
    }

    private fun initViews() {
        with(binding) {
            cancelButton = imageView6
            searchView = searchView2
            recyclerView = rv
        }
    }

    private fun handleSavedBeneficiaryItemOnClicked(data: SavedBeneficiary) {
        setFragmentResult(
            SAVED_BENEFICIARY_CLICKED_ITEM_REQUEST_KEY,
            bundleOf(SAVED_BENEFICIARY_CLICKED_ITEM_BUNDLE_KEY to data)
        )
        dismiss()
    }
}