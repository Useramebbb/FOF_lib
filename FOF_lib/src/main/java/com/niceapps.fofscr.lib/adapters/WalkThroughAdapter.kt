package com.niceapps.fofscr.lib.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.niceapps.fofscr.lib.activities.WTFullScreenAdFragment
import com.niceapps.fofscr.lib.activities.WTOneFragment
import com.niceapps.fofscr.lib.activities.WTThreeFragment
import com.niceapps.fofscr.lib.activities.WTTwoFragment
import com.niceapps.fofscr.lib.data.WalkThroughItem
import com.niceapps.fofscr.lib.interfaces.CommonEventTracker
import com.niceapps.fofscr.lib.utils.WalkThroughFragmentFactory



class WalkThroughAdapter(
    private val fragmentActivity: FragmentActivity,
    private val walkThroughItems: ArrayList<WalkThroughItem>,
    private val noOfFragments: Int,
    private val eventTracker: CommonEventTracker? = null
) : FragmentStateAdapter(fragmentActivity) {

    init {
        fragmentActivity.supportFragmentManager.fragmentFactory =
            WalkThroughFragmentFactory(walkThroughItems,eventTracker)
    }

    override fun getItemCount(): Int = noOfFragments

    override fun createFragment(position: Int): Fragment {
        val fragmentClass = when {
            noOfFragments == 4 -> when (position) {
                0 -> WTOneFragment::class.java
                1 -> WTTwoFragment::class.java
                2 -> WTFullScreenAdFragment::class.java
                3 -> WTThreeFragment::class.java
                else -> WTOneFragment::class.java
            }
            else -> when (position) {
                0 -> WTOneFragment::class.java
                1 -> WTTwoFragment::class.java
                2 -> WTThreeFragment::class.java
                else -> WTOneFragment::class.java
            }
        }

        return fragmentActivity.supportFragmentManager.fragmentFactory
            .instantiate(fragmentClass.classLoader, fragmentClass.name)
    }
}