package com.keystone.agencybankingapp.ui.view.main.openAccountScreens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.keystone.agencybankingapp.R
import com.keystone.agencybankingapp.databinding.FragmentOpenAccountDocumentationScreenBinding
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.cameraPermissionHandler
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.getDialog
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.getFileName
import com.keystone.agencybankingapp.ui.view.main.openAccountScreens.AppConstants.getImageUriFromBitmap
import pub.devrel.easypermissions.EasyPermissions

class OpenAccountDocumentationScreen : Fragment(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: FragmentOpenAccountDocumentationScreenBinding
    private lateinit var profilePicture: TextInputEditText
    private lateinit var idCard: TextInputEditText
    private lateinit var utility: TextInputEditText
    private lateinit var signature: TextInputEditText
    private lateinit var submitButton: Button

    private var currentProfileImageActivityResultAsBitMap: Bitmap? = null
    private var currentUtilityImageActivityResultAsBitMap: Bitmap? = null

    // Camera launchers
    private lateinit var profileImageCameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var signatureImageCameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var idCardImageCameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var utilityImageCameraLauncher: ActivityResultLauncher<Intent>

    // XD Card Launchers
    private lateinit var profileImageXDCardLauncher: ActivityResultLauncher<String>
    private lateinit var signatureImageXDCardLauncher: ActivityResultLauncher<String>
    private lateinit var idCardImageXDCardLauncher: ActivityResultLauncher<String>
    private lateinit var utilityImageXDCardLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_open_account_documentation_screen,
            container,
            false
        )

        initializeViews()

        // Initialize camera launchers by registering for activity result
        initializeCameraLaunchers()
        // Initialize gallery launchers by registering for activity result
        initializeGalleryLaunchers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle click
        profilePicture.handleClick(profileImageCameraLauncher, profileImageXDCardLauncher)
        signature.handleClick(signatureImageCameraLauncher, signatureImageXDCardLauncher)
        idCard.handleClick(idCardImageCameraLauncher, idCardImageXDCardLauncher)
        utility.handleClick(utilityImageCameraLauncher, utilityImageXDCardLauncher)
    }

    override fun onResume() {
        super.onResume()
        submitButton.setOnClickListener {
//            findNavController().navigate(R.id.action_newAccountOpeningViewPager_to_accountOpeningSummaryPage)
        }
    }

    private fun initializeViews() {
        profilePicture = binding.profilePicture
        idCard = binding.validId
        utility = binding.utilityBill
        signature = binding.signature
        submitButton = binding.submitBtn
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when {
            requestCode == AppConstants.STORAGE_PERM_REQUEST_CODE -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.storage_permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
            requestCode == AppConstants.CAMERA_PERM_REQUEST_CODE -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.camera_permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
            perms.size > 1 -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_granted),
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

    private fun initializeCameraLaunchers() {
        profileImageCameraLauncher = registerActivityResultLauncher(profilePicture)
        signatureImageCameraLauncher = registerActivityResultLauncher(signature)
        idCardImageCameraLauncher = registerActivityResultLauncher(idCard)
        utilityImageCameraLauncher = registerActivityResultLauncher(utility)
    }

    private fun initializeGalleryLaunchers() {
        profileImageXDCardLauncher = registerGalleryLauncher(profilePicture)
        signatureImageXDCardLauncher = registerGalleryLauncher(signature)
        idCardImageXDCardLauncher = registerGalleryLauncher(idCard)
        utilityImageXDCardLauncher = registerGalleryLauncher(utility)
    }

    private fun registerActivityResultLauncher(textInputEditText: TextInputEditText): ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val bitmap = it.data?.extras?.get("data") as Bitmap
                currentUtilityImageActivityResultAsBitMap = bitmap
                val fileName = getFileName(
                    getImageUriFromBitmap(
                        requireContext(),
                        bitmap
                    ), requireContext()
                )
                textInputEditText.setText(fileName)
            }
        }


    private fun registerGalleryLauncher(textInputEditText: TextInputEditText): ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            val bitmap = MediaStore.Images.Media.getBitmap(
                activity?.contentResolver, it
            )
            currentProfileImageActivityResultAsBitMap = bitmap
            val fileName = getFileName(it, requireContext())
            textInputEditText.setText(fileName)
        }

    @SuppressLint("ClickableViewAccessibility")
    private fun TextInputEditText.handleClick(
        cameraLauncher: ActivityResultLauncher<Intent>,
        galleryLauncher: ActivityResultLauncher<String>
    ) {
        this.setOnTouchListener(View.OnTouchListener { _, event ->
            val drawableLeft = 0
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= this.left - this.compoundDrawables[drawableLeft].bounds.width()
                ) {
                    // show Bottom Sheet
                    getDialog(
                        requireContext(),
                        resources.getStringArray(R.array.cameraOrGalleryStringArray),
                        {
                            cameraPermissionHandler(
                                this@OpenAccountDocumentationScreen,
                                requireContext()
                            ) {
                                cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                            }
                        },
                        {
                            AppConstants.galleryPermissionHandler(
                                this@OpenAccountDocumentationScreen,
                                requireContext()
                            ) {
                                galleryLauncher.launch("image/*")
                            }
                        }
                    ).show()
                    return@OnTouchListener true
                }
            }
            false
        })
    }
}