package com.example.myapplication

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.stuff.CameraActivity
import java.util.jar.Manifest

class MainActivity : CameraActivity() {

    override lateinit var previewView: PreviewView
    val PERMISSION_RC = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        previewView = findViewById(R.id.preview_view)

        checkPermission()
    }

    private fun checkPermission() {
        if (isCameraPermissionGranted()) {
            launchCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CAMERA
            )
        ) {
            //show rationale
            AlertDialog.Builder(this)
                .setTitle("Androidly Alert")
                .setMessage("Without rationale you can't use application")
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.CAMERA),
                        PERMISSION_RC
                    )
                }
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                PERMISSION_RC
            )
        }
    }

    private fun isCameraPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != PERMISSION_RC) return
        if (grantResults.size != 1) return
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Androidly Alert")
                .setMessage("You can give permission in device settings")
                .setPositiveButton(android.R.string.yes, null)
                .setNeutralButton(R.string.setting){ dialog, which ->
                    openSettings()
                }
                .show()
        }
    }
}
