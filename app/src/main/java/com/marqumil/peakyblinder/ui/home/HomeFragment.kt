package com.marqumil.peakyblinder.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marqumil.peakyblinder.databinding.FragmentHomeBinding
import com.marqumil.peakyblinder.remote.ResultState
import com.marqumil.peakyblinder.ui.article.ArticleAdapter
import com.marqumil.peakyblinder.ui.article.ArticleViewModel
import com.marqumil.peakyblinder.ui.auth.UserViewModel
import com.marqumil.peakyblinder.ui.chatbot.ChatBotActivity
import com.marqumil.peakyblinder.ui.history.AddHistoryActivity
import com.marqumil.peakyblinder.ui.retinopathy.RetinopathyActivity
import me.relex.circleindicator.CircleIndicator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var indicator: CircleIndicator
    private lateinit var articleViewModel: ArticleViewModel
    private var recyclerView: RecyclerView? = null
    private var adapter: ArticleAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val userViewModel =
            ViewModelProvider(this).get(UserViewModel::class.java)


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // hide action bar
        requireActivity().actionBar?.hide()

        binding.apply {
            // check if user username is null

            userViewModel.getUser()
            userViewModel.userState.observe(viewLifecycleOwner) {
                when (it) {
                    is ResultState.Success -> {
                        it.value.email?.let { it1 -> Log.d("user", it1) }
                        // set username
                        tvName.text = it.value.name
                    }
                    is ResultState.Failure -> {
                        Log.d("user", it.throwable.toString())
                    }
                    is ResultState.Loading -> {
                        Log.d("user", "loading")
                    }

                    else -> {}
                }
            }

            if (userViewModel.getUsername().toString() == "" || userViewModel.getUsername().toString() == "null" || userViewModel.getUsername().toString() == " ") {
                if (userViewModel.isUserSignedIn() == true) {
                    // set username
                    // cut the email name before @
                    val email = userViewModel.getEmail().toString()
                    val index = email.indexOf("@")
                    val name = email.substring(0, index)
                    tvName.text = name
                } else {
                    // get user data
                    tvName.text = "User"
                }
            } else {
                tvName.text = "User"
            }

            // cek kesehatan mental
            btnRetinopathyCheck.setOnClickListener {
                // move to other fragment
                val intent = Intent(requireContext(), RetinopathyActivity::class.java)
                startActivity(intent)
            }

        }

        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        recyclerView = binding.rvArticle

        articleViewModel.getArticleAll()
        articleViewModel.getArticle.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("ArticleFragment", "userData: ${it.value}")
                    adapter = ArticleAdapter(it.value)
                    // set horizontal layout manager
                    recyclerView?.setHasFixedSize(true)
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    recyclerView?.adapter = adapter
                }
                is ResultState.Failure -> {
                    Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("ArticleFragment", "userData: ${it.throwable.message}")
                }
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("ArticleFragment", "loading")
                }

                else -> {}
            }
        }

        return root
    }

}