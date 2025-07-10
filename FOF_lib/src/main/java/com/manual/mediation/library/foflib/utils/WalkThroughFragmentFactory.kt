package com.manual.mediation.library.foflib.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.manual.mediation.library.foflib.activities.WTFullScreenAdFragment
import com.manual.mediation.library.foflib.activities.WTOneFragment
import com.manual.mediation.library.foflib.activities.WTThreeFragment
import com.manual.mediation.library.foflib.activities.WTTwoFragment
import com.manual.mediation.library.foflib.data.WalkThroughItem
import com.manual.mediation.library.foflib.interfaces.CommonEventTracker


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