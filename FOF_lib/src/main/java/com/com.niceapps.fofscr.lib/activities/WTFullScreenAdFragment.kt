package com.manual.niceapps.fofscr.lib.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.manual.niceapps.fofscr.lib.R
import com.manual.niceapps.fofscr.lib.databinding.FragmentWalkThroughFullScreenAdmobBinding
import com.niceapps.fofscr.lib.adMobAdClasses.AdmobNativeAdFullScreen
import com.niceapps.fofscr.lib.callingClasses.FOFAdsConfigurations
import com.niceapps.fofscr.lib.callingClasses.FOFAdsManager
import com.niceapps.fofscr.lib.interfaces.CommonEventTracker


class WTFullScreenAdFragment : Fragment() {

    private lateinit var binding: FragmentWalkThroughFullScreenAdmobBinding
    private var fofAdsConfigurations: FOFAdsConfigurations? = null
    private var eventTracker: CommonEventTracker? = null
    private var handler: Handler? = null
    private lateinit var showCloseButtonRunnable: Runnable

    companion object {

        fun newInstance(tracker: CommonEventTracker? = null): WTFullScreenAdFragment {
            val fragment = WTFullScreenAdFragment()
            fragment.eventTracker = tracker
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalkThroughFullScreenAdmobBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fofAdsConfigurations = FOFAdsManager.getConfigurations()
        eventTracker?.logEvent(
            requireActivity(),
            "WTFullScreenAdFragment"
        )
        Log.i("SOTStartTestActivity", "walkthrough_fullscr")
        showCloseButtonRunnable = Runnable {
            binding.ivClose.visibility = View.VISIBLE
        }

        binding.ivClose.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 3
            binding.ivClose.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        binding.ivClose.visibility = View.GONE
        handler?.removeCallbacks(showCloseButtonRunnable)
        handler = null
    }

    override fun onResume() {
        super.onResume()
        binding.ivClose.visibility = View.GONE
        handler = Handler(Looper.getMainLooper())
        val myTime = 1000 * (fofAdsConfigurations?.getRemoteConfigData()?.get("TIMER_NATIVE_F_SRC")
            .toString().toLong() as? Long ?: 3)
        if (myTime in 1000..10000) {
            handler?.postDelayed(showCloseButtonRunnable, myTime)
        } else {
            binding.ivClose.visibility = View.GONE
        }

        val nativeWalkThroughFullScrEnabled = fofAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_FULLSCR") as? Boolean ?: false
        if (nativeWalkThroughFullScrEnabled) {
            binding.shimmerLayoutF.root.visibility = View.VISIBLE
            showAdmobWTFullNatives()
        } else {
            binding.nativeAdContainer.visibility = View.GONE
        }
    }

    private fun showAdmobWTFullNatives() {
        fofAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_FULLSCR")
            ?.let { adId ->
                AdmobNativeAdFullScreen.requestAd(
                    mContext = requireActivity(),
                    adId = adId,
                    adName = "WALKTHROUGH_FULL_SCREEN",
                    remoteConfig = fofAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_FULLSCR").toString().toBoolean(),
                    populateView = true,
                    adContainer = binding.nativeAdContainer,
                    onAdFailed = {
                        binding.nativeAdViewAdmob.visibility = View.GONE
                        binding.ivClose.performClick()
                        Log.i("NICE_APPS_ADS_TAG", "WALKTHROUGH_FULL_SCREEN: Admob: onAdFailed()")
                    },
                    onAdLoaded = {
                        binding.shimmerLayoutF.root.visibility = View.GONE
                        binding.nativeAdViewAdmob.visibility = View.VISIBLE
                        Log.i("NICE_APPS_ADS_TAG", "WALKTHROUGH_FULL_SCREEN: Admob: onAdLoaded()")
                    }
                )
            } ?: Log.w("WTOneFragment", "ADMOB_NATIVE_WALKTHROUGH_FULL_SCREEN ad ID is missing.")
    }

}