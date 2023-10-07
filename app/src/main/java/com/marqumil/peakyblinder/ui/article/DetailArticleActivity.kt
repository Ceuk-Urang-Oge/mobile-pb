package com.marqumil.peakyblinder.ui.article

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.marqumil.peakyblinder.databinding.ActivityDetailArtikelBinding
import com.marqumil.peakyblinder.remote.response.ArtikelData
import com.marqumil.peakyblinder.remote.response.ArtikelResponse

class DetailArticleActivity : AppCompatActivity() {

    companion object {
        val EXTRA_ARTIKEL = "extra_artikel"
    }

    private lateinit var binding: ActivityDetailArtikelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailArtikelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
        }

        val artikel = intent.getSerializableExtra(EXTRA_ARTIKEL) as ArtikelResponse?
        if (artikel != null) {
            Glide.with(binding.imageFilterView2.context)
                .load(artikel.image)
                .into(binding.imageFilterView2)
            binding.judul.text = artikel.title
            binding.penulis.text = artikel.authorId
            binding.isi.text = artikel.content

            Log.d("DetailArticleActivity", artikel.toString())
        }


    }


}