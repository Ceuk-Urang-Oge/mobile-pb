package com.marqumil.peakyblinder.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marqumil.peakyblinder.databinding.FragmentLastWeekBinding
import com.marqumil.peakyblinder.remote.ResultState

class LastWeekFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var _binding: FragmentLastWeekBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListHistoryAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLastWeekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllStory()
    }

    override fun onResume() {
        super.onResume()
        getAllStory()
    }

    fun getAllStory(){
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        viewModel.getGlucoseHistory()
        recyclerView = binding.rvHistory
        viewModel.getHistory.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter = ListHistoryAdapter(it.value.data)
                    recyclerView.adapter = adapter
                    recyclerView.setHasFixedSize(true)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }

                is ResultState.Failure -> {
                    binding.progressBar.visibility = View.GONE
                }

                else -> {}
            }
        }
    }

}