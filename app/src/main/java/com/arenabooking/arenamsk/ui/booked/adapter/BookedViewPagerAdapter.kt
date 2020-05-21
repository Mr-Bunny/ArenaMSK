package com.arenabooking.arenamsk.ui.booked.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.arenabooking.arenamsk.App
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.ui.booked.booked_pager_item.BookedPagerItemFragment
import com.arenabooking.arenamsk.ui.booked.booked_pager_item.BookedPagerItemFragment.Companion.CURRENT_SCREEN_TYPE
import com.arenabooking.arenamsk.ui.booked.booked_pager_item.BookedPagerItemFragment.Companion.HISTORY_SCREEN_TYPE

class BookedViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> {
            BookedPagerItemFragment.getInstance(CURRENT_SCREEN_TYPE)
        }

        else -> {
            BookedPagerItemFragment.getInstance(HISTORY_SCREEN_TYPE)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? =
        App.appContext().resources.getStringArray(R.array.booked_view_pager_titles)[position]

}