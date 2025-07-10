package com.manual.mediation.library.foflib.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.viewpager2.widget.ViewPager2
import com.manual.mediation.library.foflib.adapters.WalkThroughAdapter
import com.manual.mediation.library.foflib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.foflib.callingClasses.SOTAdsManager
import com.manual.mediation.library.foflib.databinding.ActivityWalkThroughConfigBinding
import com.manual.mediation.library.foflib.interfaces.CommonEventTracker
import com.manual.mediation.library.foflib.objects.StatusBarColor.STATUS_BAR_COLOR
import com.manual.mediation.library.foflib.utils.NetworkCheck
import com.manual.mediation.library.foflib.utils.hideSystemUIUpdated
import com.manual.mediation.library.foflib.utils.setStatusBarColor

class WalkThroughConfigActivity : AppCompatBaseActivity() {

    private lateinit var binding: ActivityWalkThroughConfigBinding
    private var sotAdsConfigurations: SOTAdsConfigurations? = null
    private lateinit var viewPager: ViewPager2
    private var previousPosition: Int = -1
    private var eventTracker: CommonEventTracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        hideSystemUIUpdated()
        setStatusBarColor(STATUS_BAR_COLOR)

        // Disable back press
        onBackPressedDispatcher.addCallback(this) {
            // Back press disabled
            Log.d("BackPress", "Back press disabled in walkthrough")
        }

        binding = ActivityWalkThroughConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sotAdsConfigurations = SOTAdsManager.getConfigurations()
        eventTracker = sotAdsConfigurations?.walkThroughScreensConfiguration?.eventTracker
        eventTracker?.logEvent(this, "sot_fof_started")

        binding.viewPager.post {
            val myNoOfFrag = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_FULLSCR")
            val noOfFragment = if (NetworkCheck.isNetworkAvailable(this) && myNoOfFrag == true) {
                4
            } else {
                3
            }

            val walkthroughList = sotAdsConfigurations
                ?.walkThroughScreensConfiguration
                ?.walkThroughList

            if (walkthroughList != null) {
                binding.viewPager.adapter = WalkThroughAdapter(
                    fragmentActivity = this,
                    walkthroughList,
                    noOfFragment,
                    eventTracker
                )

            }

            setupPageChangeListener()
        }
    }

    private fun setupPageChangeListener() {
        viewPager = binding.viewPager
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (previousPosition != -1) {
                    when (previousPosition) {
                        0 -> if (position == 1) {
                            eventTracker?.logEvent(
                                this@WalkThroughConfigActivity,
                                "walkthrough1_scr"
                            )
                            Log.i("WalkThrough", "0 → 1")
                        }
                        1 -> {
                            if (position == 2) {
                                Log.i("WalkThrough", "1 → 2")
                            } else if (position == 0) {
                                eventTracker?.logEvent(this@WalkThroughConfigActivity, "walkthrough2_scr_swipe_back")
                                Log.i("WalkThrough", "1 → 0")
                            }
                        }
                        2 -> if (position == 1) {
                            Log.i("WalkThrough", "2 → 1")
                        }
                    }
                }
                previousPosition = position
            }
        })
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUIUpdated()
    }
}
