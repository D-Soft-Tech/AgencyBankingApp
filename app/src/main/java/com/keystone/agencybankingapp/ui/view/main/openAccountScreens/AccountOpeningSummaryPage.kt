package com.keystone.agencybankingapp.ui.view.main.openAccountScreens

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentAccountOpeningSummaryPageBinding
import com.keystone.agencybankingapp.databinding.OpenAccountSummaryPdfLayoutBinding
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.WRITE_PERMISSION_REQUEST_CODE
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.genericPermissionHandler
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.getDormieAccountSummary
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream

class AccountOpeningSummaryPage : Fragment(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: FragmentAccountOpeningSummaryPageBinding
    private lateinit var pdfView: OpenAccountSummaryPdfLayoutBinding
    private lateinit var accountDetails: File
    private lateinit var downloadImage: ImageView
    private lateinit var shareImageView: ImageView
    private lateinit var accountName: TextView
    private lateinit var accountNumber: TextView
    private lateinit var accountType: TextView
    private lateinit var accountOfficerName: TextView
    private lateinit var accountOfficerPhoneNumber: TextView
    private lateinit var finishBtn: Button
    private lateinit var backArrow: ImageView
    private lateinit var backTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account_opening_summary_page,
            container,
            false
        )

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                findNavController().navigate(R.id.action_accountOpeningSummaryPage_to_dashboardNew)
//                findNavController()
            }

        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()

        // Pdf layout inflater
        val inflater = LayoutInflater.from(context)
        pdfView = OpenAccountSummaryPdfLayoutBinding.inflate(inflater)
        val pdFData = getDormieAccountSummary()

        // Assign values to layout
        pdfView.apply {
            accountName.text = getString(R.string.account_name_place_holder, pdFData.accName)
            accountNumber.text = getString(R.string.account_number_place_holder, pdFData.accNumber)
            accountType.text = getString(R.string.account_type_place_holder, pdFData.accType)
            accountOfficerName.text =
                getString(R.string.account_officer_name_place_holder, pdFData.accOfficer)
            accountOfficerNumber.text =
                getString(R.string.account_officer_phone_number_place_holder, pdFData.accOfficerNum)
        }
    }

    override fun onResume() {
        super.onResume()

        downloadImage.setOnClickListener {
            genericPermissionHandler(
                this@AccountOpeningSummaryPage,
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                WRITE_PERMISSION_REQUEST_CODE,
                getString(R.string.storage_permission_rationale_for_download),
            ) {
                createPdf()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fileDownloaded),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        shareImageView.setOnClickListener {
            // First create the Pdf
            createPdf()
            sharePdf(accountDetails)
        }

        finishBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_accountOpeningSummaryPage_to_dashboardNew)
        }

        backTextView.setOnClickListener {
//            findNavController().navigate(R.id.action_accountOpeningSummaryPage_to_dashboardNew)
        }

        backArrow.setOnClickListener {
//            findNavController().navigate(R.id.action_accountOpeningSummaryPage_to_dashboardNew)
        }
    }

    private fun initializeViews() {
        downloadImage = binding.downLoadIcon
        shareImageView = binding.shareIcon
        accountName = binding.accountName
        accountNumber = binding.accountNumber
        accountType = binding.accountType
        accountOfficerName = binding.accountOfficerName
        accountOfficerPhoneNumber = binding.accountOfficerPhoneNumber
        finishBtn = binding.submitBtn
        backArrow = binding.backArrow
        backTextView = binding.backText
    }

    private fun createPdf() {
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context?.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi
        } else {
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        }

        pdfView.root.measure(
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
            )
        )

        pdfView.root.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)

        val bitmap = view?.measuredWidth?.let {
            pdfView.root.measuredHeight.let { it1 ->
                Bitmap.createBitmap(
                    it,
                    it1, Bitmap.Config.ARGB_8888
                )
            }
        }

        val canvas = bitmap?.let { Canvas(it) }
        pdfView.root.draw(canvas)

        if (bitmap != null) {
            Bitmap.createScaledBitmap(bitmap, 595, 842, true)
        }

        val pdfDocument = PdfDocument()
        val pageInfo = bitmap?.let { PdfDocument.PageInfo.Builder(it.width, it.height, 1).create() }

        val page = pdfDocument.startPage(pageInfo)
        if (bitmap != null) {
            page.canvas.drawBitmap(bitmap, 0F, 0F, null)
        }
        pdfDocument.finishPage(page)

        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            getDormieAccountSummary().accName + ".pdf"
        )

        Log.d("pdfNew", "$filePath")

        accountDetails = filePath
        pdfDocument.writeTo(FileOutputStream(filePath))

        pdfDocument.close()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            AppConstants.STORAGE_PERM_REQUEST_CODE -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.storage_permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(requireContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    // share the pdf file
    private fun sharePdf(outputFile: File) {

        // This is the way to do it for targetSdkVersion < 24
//        val uri: Uri = Uri.fromFile(outputFile)
        // For targetSdkVersion >= 24
        val uri: Uri = FileProvider.getUriForFile(
            requireContext(),
            context?.applicationContext?.packageName + ".provider",
            outputFile
        )
        val share = Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        startActivity(share)
    }
}