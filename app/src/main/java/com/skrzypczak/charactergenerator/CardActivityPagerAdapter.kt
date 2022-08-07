package com.skrzypczak.charactergenerator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.skrzypczak.charactergenerator.ui.ObverseFragment
import com.skrzypczak.charactergenerator.ui.ReverseFragment

class CardActivityPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    companion object {
        private const val NUM_PAGES = 2
    }

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ObverseFragment()
        1 -> ReverseFragment()
        else -> throw IllegalArgumentException("Page doesn't exist")
    }
}