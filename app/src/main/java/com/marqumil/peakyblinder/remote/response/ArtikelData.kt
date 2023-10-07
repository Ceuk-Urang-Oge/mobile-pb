package com.marqumil.peakyblinder.remote.response

import java.io.Serializable

data class ArtikelData(
    val judul: String,
    val tanggalTerbit: String,
    val author: String,
    val isi: String,
    val img: Int
) : Serializable


