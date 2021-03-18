package com.example.myapplication.stuff

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.myapplication.BuildConfig
import com.google.common.util.concurrent.ListenableFuture

abstract class CameraActivity : AppCompatActivity() {

    abstract var previewView: PreviewView
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    }

    protected fun launchCamera() {
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    protected fun openSettings() {
        startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            )
        )
    }

    protected fun showAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("Androidly Alert")
            .setMessage("We have a message: without rationale you can't use application")
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 123)
            }
            .setNegativeButton(android.R.string.no) { dialog, which ->

            }
            .setNeutralButton("Maybe") { dialog, which ->

            }
            .show()
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)

        preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.cameraInfo))
    }

}