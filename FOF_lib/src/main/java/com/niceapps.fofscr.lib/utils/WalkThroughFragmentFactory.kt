package com.niceapps.fofscr.lib.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.niceapps.fofscr.lib.activities.WTFullScreenAdFragment
import com.niceapps.fofscr.lib.activities.WTOneFragment
import com.niceapps.fofscr.lib.activities.WTThreeFragment
import com.niceapps.fofscr.lib.activities.WTTwoFragment
import com.niceapps.fofscr.lib.data.WalkThroughItem
import com.niceapps.fofscr.lib.interfaces.CommonEventTracker


class WalkThroughFragmentFactory(
    private val walkThroughItems: ArrayList<WalkThroughItem>,
    private val eventTracker: CommonEventTracker? = null
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            WTOneFragment::class.java.name -> WTOneFragment.newInstance(walkThroughItems[0], eventTracker)
            WTTwoFragment::class.java.name -> WTTwoFragment.newInstance(walkThroughItems[1],eventTracker)
            WTThreeFragment::class.java.name -> WTThreeFragment.newInstance(walkThroughItems[2],eventTracker)
            WTFullScreenAdFragment::class.java.name -> WTFullScreenAdFragment.newInstance(eventTracker)
            else -> super.instantiate(classLoader, className)
        }
    }
}