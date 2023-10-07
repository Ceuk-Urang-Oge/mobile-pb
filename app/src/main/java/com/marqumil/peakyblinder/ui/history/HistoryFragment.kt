package com.marqumil.peakyblinder.ui.history

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ReturnThis
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.marqumil.peakyblinder.R
import com.marqumil.peakyblinder.databinding.FragmentHistoryBinding
import com.marqumil.peakyblinder.remote.ResultState

class HistoryFragment : Fragment() {


    private lateinit var viewModel: HistoryViewModel
    private lateinit var _binding: FragmentHistoryBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentStateAdapter

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tabLayout = binding.tablayout
        viewPager2 = binding.viewpager
        adapter = HistoryPageAdapter(this, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("last week"))
        tabLayout.addTab(tabLayout.newTab().setText("last month"))

        viewPager2.adapter = adapter
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // finish this
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })


        binding.apply {
            btnAdd.setOnClickListener {
                val intent = Intent(requireContext(), AddHistoryActivity::class.java)
                startActivity(intent)
                // finish this fragment

            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getHistory()
    }

    @SuppressLint("SetTextI18n")
    fun getHistory() {
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        viewModel.getGlucoseHistory()
        viewModel.getHistory.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Loading -> {

                }

                is ResultState.Success -> {
                    binding.apply {
                        if (it.value.bloodGlucoseAverage != null) {
                            val number = it.value.bloodGlucoseAverage.toInt()
                            persentase.text = "$number md/dl"

                            val background = it.value.bloodGlucoseLevel
                            if (background == "Rendah") {
                                level.setBackgroundColor(Color.YELLOW)
                            }else if (background == "Normal"){
                                level.setBackgroundColor(Color.GREEN)
                            }else if(background == "Tinggi"){
                                level.setBackgroundColor(Color.YELLOW)
                            } else if(background == "Sangat Tinggi"){
                                level.setBackgroundColor(Color.RED)
                            }
                            textLevel.text = it.value.bloodGlucoseLevel
                        } else {
                            persentase.text = "0 md/dl"
                            textLevel.text = "Empty"
                        }
                    }
                }

                is ResultState.Failure -> {

                }

                else -> {}
            }
        }
    }


}