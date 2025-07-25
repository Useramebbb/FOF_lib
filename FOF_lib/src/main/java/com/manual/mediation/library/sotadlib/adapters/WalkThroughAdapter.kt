package com.manual.mediation.library.sotadlib.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.manual.mediation.library.sotadlib.activities.WTFullScreenAdFragment
import com.manual.mediation.library.sotadlib.activities.WTOneFragment
import com.manual.mediation.library.sotadlib.activities.WTThreeFragment
import com.manual.mediation.library.sotadlib.activities.WTTwoFragment
import com.manual.mediation.library.sotadlib.data.WalkThroughItem
import com.manual.mediation.library.sotadlib.interfaces.CommonEventTracker
import com.manual.mediation.library.sotadlib.utils.WalkThroughFragmentFactory



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