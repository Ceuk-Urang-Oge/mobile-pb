package com.marqumil.peakyblinder.ui.article

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marqumil.peakyblinder.databinding.FragmentArticleBinding
import com.marqumil.peakyblinder.remote.ResultState

class ArticleFragment : Fragment() {

    private lateinit var viewModel: ArticleViewModel
    private lateinit var _binding: FragmentArticleBinding
    private lateinit var recyclerView: RecyclerView

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root


        viewModel = ArticleViewModel()

        recyclerView = binding.rvArticle

        viewModel.getArticleAll()
        viewModel.getArticle.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("article", it.value.toString())
                    val data = it.value
                    recyclerView.adapter = ArticleAdapter(data)
                }
                is ResultState.Failure -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
                    Log.d("article", it.throwable.toString())
                }
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("article", "Loading")
                }
            }
        }


        recyclerView.setHasFixedSize(true)

        // Initialize the adapter here
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return root
    }

}