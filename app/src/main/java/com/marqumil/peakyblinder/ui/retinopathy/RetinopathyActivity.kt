package com.marqumil.peakyblinder.ui.retinopathy

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.marqumil.peakyblinder.R
import com.marqumil.peakyblinder.databinding.ActivityRetinopathyBinding
import com.marqumil.peakyblinder.remote.ResultState
import com.marqumil.peakyblinder.ui.utils.ConfirmDialogRetinopathy
import java.io.File

class RetinopathyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRetinopathyBinding
    private lateinit var viewModel: RetinopathyViewModel

    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRetinopathyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = RetinopathyViewModel()

        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }

            btnGallery.setOnClickListener {
                ImagePicker.with(this@RetinopathyActivity)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start()

            }

            btnPredict.setOnClickListener {
                viewModel.postRetinopathy()
                viewModel.postRetinopathy.observe(this@RetinopathyActivity) {
                    when (it) {
                        is ResultState.Success -> {
                            binding.progressBar.visibility = android.view.View.GONE
                            Toast.makeText(this@RetinopathyActivity, "Success", Toast.LENGTH_SHORT).show()
                            Log.d("RetinopathyActivity", "onCreate: ${it.value.kumilcintabh}")
                            binding.btnPredict.isEnabled = false
                            binding.btnPredict.text = "Success"

                            // set confirm dialog
                            val dialog = ConfirmDialogRetinopathy()
                            dialog.description = it.value.label.toString()
                            dialog.positiveText = "Next"
                            dialog.positiveCallback = {
                                val intent = Intent(this@RetinopathyActivity, DetailRetinopathyActivity::class.java)
                                intent.putExtra(DetailRetinopathyActivity.EXTRA_RETINOPATHY, it.value)
                                intent.putExtra(DetailRetinopathyActivity.EXTRA_URI, uri)
                                startActivity(intent)
                                dialog.dismiss()
                                finish()
                            }
                            dialog.negativeText = "Cancel"
                            dialog.negativeCallback = {
                                dialog.dismiss()
                            }
                            dialog.show(supportFragmentManager, "ConfirmDialogRetinopathy")

                        }
                        is ResultState.Failure -> {
                            Toast.makeText(this@RetinopathyActivity, "Failure", Toast.LENGTH_SHORT).show()
                        }
                        is ResultState.Loading -> {
                            binding.btnPredict.isEnabled = false
                            binding.btnPredict.text = "Loading"
                            binding.progressBar.visibility = android.view.View.VISIBLE
                            Toast.makeText(this@RetinopathyActivity, "Loading", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            uri.let { this.uri = it }
            // Use Uri object instead of File to avoid storage permissions
            binding.imageView.setImageURI(uri)

            // parse uri to file
            val file = uri.path?.let { File(it) }

            viewModel.getFile = file

            binding.btnPredict.visibility = android.view.View.VISIBLE
            binding.tvAnalyzing.visibility = android.view.View.GONE
            binding.tvAnalyzingDesc.visibility = android.view.View.GONE
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

}