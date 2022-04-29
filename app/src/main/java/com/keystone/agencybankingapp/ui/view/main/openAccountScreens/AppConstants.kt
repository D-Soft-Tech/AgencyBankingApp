package com.keystone.agencybankingapp.ui.view.main.openAccountScreens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.hardware.biometrics.BiometricPrompt
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.keystone.agencybankingapp.R
import kotlinx.coroutines.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


object AppConstants {

    data class AccountSummary(
        var accName: String,
        var accNumber: String,
        var accType: String,
        var accOfficer: String,
        var accOfficerNum: String
    )

    const val STORAGE_PERM_REQUEST_CODE = 111
    const val WRITE_PERMISSION_REQUEST_CODE = 113
    const val CAMERA_PERM_REQUEST_CODE = 112
    const val PHONE_CONTACT_PERM_REQUEST_CODE = 114
    const val PHONE_CALL_PERMISSION_CODE = 115
    const val LOADING_DIALOG_FRAGMENT_TAG = "LOADING"
    const val AGENT_PIN_REQUEST_KEY = "agentPinRequestKey"
    const val AGENT_PIN_DIALOG_TAG = "agentPinTag"
    const val AGENT_PIN_BUNDLE_KEY = "agentPinBundleKey"
    const val SAVED_BENEFICIARY_BOTTOM_SHEET_TAG = "SavedBeneficiary"
    const val SAVED_BENEFICIARY_CLICKED_ITEM_REQUEST_KEY = "selectedSavedBeneficiaryRK"
    const val SAVED_BENEFICIARY_CLICKED_ITEM_BUNDLE_KEY = "selectedSavedBeneficiaryBK"

    fun getDormieAccountSummary() =
        AccountSummary(
            "OLOYEDE ADEBAYO OLAWALE",
            "1234567890",
            "Individual Savings Account",
            "Olise Elizabeth",
            "09087654321"
        )

    fun getDialog(context: Context, items: Array<String>, camera: () -> Unit, gallery: () -> Unit) =
        MaterialAlertDialogBuilder(context)
            .setTitle(context.resources.getString(R.string.cameraOrGallery))
            .setItems(items)
            { dialog, which ->
                when (which) {
                    0 -> {
                        dialog.dismiss()
                        camera()
                    }
                    1 -> {
                        dialog.dismiss()
                        gallery()
                    }
                }
            }

    fun getCustomInfoDialog(
        context: Context,
        layout: Int? = null,
        title: String? = null,
        message: String? = null,
        yesOrNoActionRequired: Boolean = false
    ): AlertDialog =
        AlertDialog.Builder(context).apply {
            layout?.let {
                setView(it)
            }
            if (yesOrNoActionRequired) {
                setTitle(title)
                setMessage(message)
            }
        }.create()

    fun View.delayOnLifecycle(
        delayTimeInMilliSec: Long,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        actionToRun: () -> Unit
    ): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
        lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
            delay(delayTimeInMilliSec)
            actionToRun()
        }
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val fileName = SimpleDateFormat("yy-MM-dd-HH-mm-ss", Locale.getDefault()).format(
            System.currentTimeMillis()
        )
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, fileName, null)
        return Uri.parse(path.toString())
    }

    @SuppressLint("Range")
    fun getFileName(uri: Uri, context: Context): String? {
        var result: String? = null
        if (uri.scheme.equals("content")) {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            cursor.use { cursor ->
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        result =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                if (cut != null) {
                    result = result?.substring(cut + 1)
                }
            }
        }
        return result
    }

    fun cameraPermissionHandler(host: Fragment, context: Context, fn: () -> Unit) {
        if (checkForPermission(context, Manifest.permission.CAMERA)) {
            fn()
        } else {
            requestForPermission(
                host,
                CAMERA_PERM_REQUEST_CODE,
                context.resources.getString(R.string.camera_permission_rationale),
                Manifest.permission.CAMERA
            )
        }
    }

    fun galleryPermissionHandler(host: Fragment, context: Context, fn: () -> Unit) {
        if (checkForPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            fn()
        } else {
            requestForPermission(
                host,
                STORAGE_PERM_REQUEST_CODE,
                context.resources.getString(R.string.storage_permission_rationale),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun checkForPermission(context: Context, perms: String) =
        EasyPermissions.hasPermissions(
            context,
            perms
        )

    fun genericPermissionHandler(
        host: Fragment,
        context: Context,
        perm: String,
        permCode: Int,
        permRationale: String,
        fn: () -> Unit
    ) {
        if (checkForPermission(context, perm)) {
            fn()
        } else {
            requestForPermission(
                host,
                permCode,
                permRationale,
                perm
            )
        }
    }

    private fun requestForPermission(
        host: Fragment,
        requestCode: Int,
        permissionRationale: String,
        permissionToRequest: String
    ) {
        EasyPermissions.requestPermissions(
            host,
            permissionRationale,
            requestCode,
            permissionToRequest
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBiometricSupport(
        activity: Activity?,
        context: Context,
        rootView: View
    ): Boolean {
        val keyguardManager: KeyguardManager =
            activity?.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure) {
            Snackbar.make(
                context,
                rootView,
                activity.resources.getString(R.string.fingerPrintNotEnabled),
                Snackbar.LENGTH_SHORT
            ).show()
            return false
        }
        return activity.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    fun notifyUserWithSnackBar(
        message: String,
        context: Context,
        rootView: View
    ) {
        Snackbar.make(
            context,
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getAuthenticationCallBack(
        activity: Activity?,
        context: Context,
        rootView: View,
        actionAfterSuccess: () -> Unit
    ) =
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                notifyUserWithSnackBar("Authentication error: $errString", context, rootView)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                notifyUserWithSnackBar("Authentication successful", context, rootView)
                // If the authentication is successful, navigate user into the main app
                actionAfterSuccess()
            }
        }

    // FingerPrint Object
    @RequiresApi(Build.VERSION_CODES.P)
    private fun getFingerPrintObject(context: Context) = BiometricPrompt.Builder(context).apply {
        setTitle(context.resources.getString(R.string.biometricTitle))
        setSubtitle(context.resources.getString(R.string.biometricSubTitle))
        setDescription(context.resources.getString(R.string.biometricDescription))
        setNegativeButton(
            context.resources.getString(R.string.cancel),
            context.mainExecutor,
            DialogInterface.OnClickListener { _, _ -> })
    }.build()

    @RequiresApi(Build.VERSION_CODES.P)
    fun runFingerPrintScanner(
        context: Context,
        rootView: View,
        activity: Activity?,
        cancellationSignal: CancellationSignal,
        actionAfterSuccess: () -> Unit
    ) {
        if (checkBiometricSupport(activity, context, rootView)) {
            if (activity != null) {
                getFingerPrintObject(context).authenticate(
                    cancellationSignal,
                    activity.mainExecutor,
                    getAuthenticationCallBack(activity, context, rootView, actionAfterSuccess)
                )
            }
        }
    }

    private fun Context.hideKeyboard(view: View) {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            view.windowToken,
            0
        )
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun validateInputFieldsNotEmpty(context: Context, rootView: View, vararg inputFields: EditText, action: () -> Unit) {
        if (inputFields.any { it.text.isNullOrEmpty() }) {
            Snackbar.make(
                context,
                rootView,
                context.getString(R.string.allFieldsRequired),
                Snackbar.LENGTH_SHORT
            ).show()
        } else {
            action()
        }
    }
}
