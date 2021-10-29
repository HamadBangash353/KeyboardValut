package com.example.keyboardvalut.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
import java.util.*

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


        if (checkPermissionStatus()) {
            startCamera()
        }
        binding?.clickHandler = this

    }


    private fun verifyingPassword() {

        val input: String = binding?.etPassword?.text.toString()
        val toast: Toast = Toast.makeText(context, "Please enter input", Toast.LENGTH_SHORT)

        if (input == "") {
            toast.show()
        } else {
            if (input == prefUtil.password) {
                toast.cancel()
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
                if (input != prefUtil.password) {
                    Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show()
                    binding?.etPassword?.setText("")
                }
            }


        }

        if (input.lowercase(Locale.US) == "forgot password" || input.lowercase(Locale.US) == "forget password" ||
            input.lowercase(Locale.US) == "forgetpassword" ||
            input.lowercase(Locale.US) == "forgotpassword"
        ) {

            startActivity(Intent(context, ForgotPasswordActivity::class.java))
            binding!!.etPassword.setText("")
        } else {
            if (input.length > prefUtil.password.length) {
                Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionStatus(): Boolean {
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
            R.id.ivEnter -> {
                verifyingPassword()

            }
            R.id.btnEnableKeyboard -> {
                val enableIntent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                enableIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                this.startActivity(enableIntent)

            }
            R.id.btnSelectKeyboard -> {
                val imeManager =
                    applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imeManager.showInputMethodPicker()

            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()
            preview!!.setSurfaceProvider(binding?.myCameraVeiw?.createSurfaceProvider(camera?.cameraInfo))
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                    .build()
            cameraProvider.unbindAll()
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
                    Log.d("MyImageError", "ERROR")
                }
            })
    }

    override fun onResume() {
        super.onResume()

        binding?.etPassword?.requestFocus()
        binding?.etPassword?.setText("")

    }

    override fun onBackPressed() {
        showingExitDialog()
    }

    private fun showingExitDialog() {
        AlertDialog.Builder(this)
            .setMessage("Do you really want to exit?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->

//                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//                        homeIntent.addCategory(Intent.CATEGORY_HOME);
//                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(homeIntent);
//                        System.exit(1);
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                startActivity(intent)
            }
            .setNegativeButton("No", null)
            .show()
    }
}