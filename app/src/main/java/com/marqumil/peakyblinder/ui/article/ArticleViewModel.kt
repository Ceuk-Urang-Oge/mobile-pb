package com.marqumil.peakyblinder.ui.article

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marqumil.peakyblinder.R
import com.marqumil.peakyblinder.remote.ResultState
import com.marqumil.peakyblinder.remote.response.ArtikelData
import com.marqumil.peakyblinder.remote.response.ArtikelResponse
import com.marqumil.peakyblinder.remote.response.PhotoResponse
import com.marqumil.peakyblinder.remote.response.PostArticleResponse
import com.marqumil.peakyblinder.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    private val _getArticle = MutableLiveData<ResultState<List<ArtikelResponse>>>()
    val getArticle: MutableLiveData<ResultState<List<ArtikelResponse>>> = _getArticle

    private val _postArticle = MutableLiveData<ResultState<PostArticleResponse>>()
    val postArticle: MutableLiveData<ResultState<PostArticleResponse>> = _postArticle

    private val _photoArticle = MutableLiveData<ResultState<PhotoResponse>>()
    val photoArticle: MutableLiveData<ResultState<PhotoResponse>> = _photoArticle

    private val _forceLogout = MutableLiveData(false)
    val forceLogout: MutableLiveData<Boolean> = _forceLogout

    fun getArticleAll() {
        viewModelScope.launch {
            _getArticle.value = ResultState.Loading
            try {
                val response = ApiConfig.getApiService().getArticleAll()
                val data = response.data
                if (data != null) {
                    Log.d("data", data.toString())
                    _getArticle.postValue(ResultState.Success(data))
                } else {
                    _getArticle.postValue(ResultState.Failure(Throwable("Data Kosong")))
                }
            } catch (e: Exception) {
                _getArticle.postValue(ResultState.Failure(Throwable(e.message)))
            }
        }
    }

    fun getArticle(): ArrayList<ArtikelData> {
        // array of Artikel Data
        val article = ArrayList<ArtikelData>()

        // adding Artikel Data to ArrayList
        val article1 = ArtikelData(
            "Panduan Makan Sehat untuk Pengidap Diabetes",
            "10 Januari 2023",
            "Dr. Maria Susanti, Sp.PD",
            "Panduan makan sehat bagi penderita diabetes sangat penting untuk menjaga gula darah tetap stabil. Beberapa tips termasuk membatasi konsumsi karbohidrat sederhana, meningkatkan serat dalam makanan, dan memilih makanan rendah gula. Selain itu, penting untuk menghindari makanan cepat saji dan minuman beralkohol.",
            R.drawable.artikel_1
        )

        val article2 = ArtikelData(
            "Pentingnya Olahraga dalam Pengelolaan Diabetes",
            "22 Februari 2023",
            "Fitriani Suryanto, PT",
            "Olahraga adalah bagian penting dari manajemen diabetes. Dalam artikel ini, kami membahas jenis-jenis olahraga yang cocok bagi penderita diabetes, seperti berjalan kaki, bersepeda, dan renang. Olahraga secara teratur dapat membantu meningkatkan sensitivitas insulin dan mengontrol kadar gula darah.",
            R.drawable.artikel_2
        )

        val article3 = ArtikelData(
            "Tips Menyusun Menu Makanan Rendah Karbohidrat untuk Diabetes",
            "14 Mei 2023",
            "Chef Ahmad Abdullah",
            "Menyusun menu makanan rendah karbohidrat dapat membantu mengendalikan diabetes. Artikel ini berisi resep-resep lezat dan ide-ide makanan yang rendah karbohidrat namun kaya nutrisi. Dengan mengikuti panduan ini, Anda dapat menjaga gula darah tetap stabil.",
            R.drawable.artikel_3
        )

        val article4 = ArtikelData(
            "Teknologi Terkini dalam Pengukuran Gula Darah",
            "10 Maret 2023",
            "Dr. Budi Rahardjo, Sp.PD, M.Kes",
            "Teknologi terbaru dalam pengukuran gula darah, seperti sensor kontinu dan aplikasi pemantauan, telah membuat manajemen diabetes lebih mudah. Dalam artikel ini, kami menjelaskan cara teknologi ini bekerja dan bagaimana pengidap diabetes dapat menggunakannya untuk memantau kondisi mereka.",
            R.drawable.artikel_1
        )

        val article5 = ArtikelData(
            "Menjaga Kesehatan Mata Saat Diabetes",
            "17 Juni 2020",
            "Dr. Lina Wijaya, Sp.M",
            "Diabetes dapat menyebabkan masalah kesehatan mata, seperti retinopati diabetik. Dalam artikel ini, kami membahas pentingnya pemeriksaan mata rutin dan memberikan tips untuk menjaga kesehatan mata saat diabetes.",
            R.drawable.artikel_2
        )

        val article6 = ArtikelData(
            "Manajemen Stres untuk Pengidap Diabetes",
            "12/12/2020",
            "Psikolog Nia Ramadhani, M.Psi",
            "Stres dapat memengaruhi kadar gula darah. Dalam artikel ini, psikolog memberikan tips tentang cara mengelola stres dan menghindari dampak negatifnya pada diabetes."
            , R.drawable.artikel_3
        )

        // adding Artikel Data to ArrayList
        article.addAll(listOf(article1, article2, article3, article4, article5, article6))

        return article
    }
}