package com.marqumil.peakyblinder.ui.retinopathy

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marqumil.peakyblinder.R
import com.marqumil.peakyblinder.databinding.ActivityDetailRetinopathyBinding
import com.marqumil.peakyblinder.remote.response.RetinopathyResponse

class DetailRetinopathyActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailRetinopathyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailRetinopathyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
        }

        val retinopathy = intent.getParcelableExtra(EXTRA_RETINOPATHY) as RetinopathyResponse?
        val uri = intent.getParcelableExtra(EXTRA_URI) as Uri?
        binding.apply {
            tvTitle.text = retinopathy?.kumilcintabh?.title ?: "No Title"
            // set list to string
            val descGeneral = retinopathy?.kumilcintabh?.generalRecommendation?.joinToString(".") ?: "No Description"
            // split string by dot and enter the text
            val parts = descGeneral.split(".")
            val sb = StringBuilder()

            for (i in parts.indices) {
                sb.append(parts[i])
                sb.append("\n")
            }
            val desc = sb.toString()
            tvDescGeneral.text = desc

            val descSpecific = retinopathy?.kumilcintabh?.specificRecomendation?.joinToString(".") ?: "No Description"
            val parts2 = descSpecific.split(".")
            val sb2 = StringBuilder()

            for (i in parts2.indices) {
                sb2.append(parts2[i])
                sb2.append("\n")
            }

            val desc2 = sb2.toString()

            tvDescInsight.text = desc2

            // set image from uri
            shapeableImageView.setImageURI(uri)
        }

    }

    companion object {
        const val EXTRA_RETINOPATHY = "extra_retinopathy"
        const val EXTRA_URI = "extra_uri"
    }


}

