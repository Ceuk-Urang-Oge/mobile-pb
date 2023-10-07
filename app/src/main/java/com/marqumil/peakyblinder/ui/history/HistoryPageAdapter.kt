package com.marqumil.peakyblinder.ui.history

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HistoryPageAdapter(
    fragmentManager: Fragment,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager.childFragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            LastWeekFragment()
        else
            LastMonthFragment()
    }
}