package com.example.keyboardvalut.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.keyboardvalut.R
import com.example.keyboardvalut.databinding.ActivityEnterVaultPasswordBinding
import com.example.keyboardvalut.interfaces.ClickListener
import com.example.keyboardvalut.utils.ScreenUtils
import com.example.keyboardvalut.utils.SharedPrefUtil
import java.io.File


class VaultPasswordEnteringActivity : AppCompatActivity(), ClickListener {

    lateinit var context: Context
    var camera: androidx.camera.core.Camera? = null
    var preview: Preview? = null
    var imageCapture: ImageCapture? = null

    lateinit var prefUtil: SharedPrefUtil

    var isSnapped: Boolean = false


    var binding: ActivityEnterVaultPasswordBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter_vault_password)
        ScreenUtils.hidingStatusBar(this)
        context = this

        prefUtil = SharedPrefUtil(context)


        Log.d("MyPermissionStatus", checkPermissionStatus().toString());

        if (checkPermissionStatus()) {
            startCamera()
        }
        verifyingPassword()
        binding?.clickHandler = this


    }

    private fun verifyingPassword() {
        binding?.etPassword?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (s.toString().length == prefUtil.password.length) {
                    if (s.toString() == prefUtil.password) {
                        passingIntentToVaultMainActivity()
                    } else {
                        if (prefUtil.breakInAlert) {
                            if (checkPermissionStatus()) {
                                if (!isSnapped) {
                                    savingImage()
                                    isSnapped = true
                                }
                            }
                        }
                    }
                }
            }

        })
    }

    fun checkPermissionStatus(): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED)
    }


    private fun passingIntentToVaultMainActivity() {
        intent = Intent(context, VaultMainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnPhotosVault -> {
                intent = Intent(context, VaultSettingsActivity::class.java)
                startActivity(intent)

            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()
            preview!!.setSurfaceProvider(binding?.myCameraVeiw?.createSurfaceProvider(camera?.cameraInfo))
            imageCapture = ImageCapture.Builder().build();
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                    .build();
            cameraProvider.unbindAll();
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

        }, ContextCompat.getMainExecutor(context))

    }

    fun savingImage() {

        val sdCard = Environment.getExternalStorageDirectory()
        val dir = File(
            sdCard.absolutePath,
            "/.KeyboardVault/wrongPassImages/" + System.currentTimeMillis() + ".jpg" + ""
        )
        val outPut = ImageCapture.OutputFileOptions.Builder(dir).build()
        imageCapture?.takePicture(
            outPut,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.d("MyImageError", "ERROR");
                }
            });
    }
}