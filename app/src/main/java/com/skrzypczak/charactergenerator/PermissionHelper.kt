package com.skrzypczak.charactergenerator

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionHelper(private val context: Context) {

    fun hasReadMediaPermission() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED

    fun startReadMediaPermissionFlow(activity: Activity, resultLauncher: ActivityResultLauncher<String>) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE

        if (activity.shouldShowRequestPermissionRationale(permission)) {
            showRationale(activity, resultLauncher, permission)
        } else {
            resultLauncher.launch(permission)
        }
    }

    private fun showRationale(activity: Activity, resultLauncher: ActivityResultLauncher<String>, permission: String) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.read_permission_title)
            .setMessage(R.string.read_permission_message)
            .setNegativeButton(R.string.read_permission_cancel) { _, _ ->
                activity.finish()
            }
            .setPositiveButton(R.string.read_permission_accept) { _, _ ->
                resultLauncher.launch(permission)
            }
            .show()
    }
}