package com.manual.mediation.library.foflib.activities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.manual.mediation.library.foflib.R
import com.manual.mediation.library.foflib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.foflib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.foflib.callingClasses.SOTAdsManager
import com.manual.mediation.library.foflib.data.WalkThroughItem
import com.manual.mediation.library.foflib.databinding.FragmentWTTwoBinding
import com.manual.mediation.library.foflib.interfaces.CommonEventTracker

import com.manual.mediation.library.foflib.utils.NetworkCheck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WTTwoFragment : Fragment() {
    private lateinit var binding: FragmentWTTwoBinding
    private var sotAdsConfigurations: SOTAdsConfigurations? = null
    private lateinit var item: WalkThroughItem
    private var eventTracker: CommonEventTracker? = null
    private var scaleType :Int = 0
    companion object {
        private const val ARG_ITEM = "walkThroughItem"

        fun newInstance(item: WalkThroughItem,tracker: CommonEventTracker? = null): WTTwoFragment {
            val fragment = WTTwoFragment()

                val args = Bundle().apply {
                    putParcelable(ARG_ITEM, item)
                }
                fragment.arguments = args
                fragment.eventTracker = tracker
                return fragment
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = arguments?.getParcelable(ARG_ITEM) ?: throw IllegalStateException("WalkThroughItem must be provided")
        scaleType =item.imageScale
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWTTwoBinding.inflate(inflater, container, false)
        eventTracker?.logEvent(
            requireActivity(),
            "walkthrough2_scr"
        )
        if (scaleType == 0){
            binding.main.visibility = View.GONE
            binding.mainCopy.visibility = View.VISIBLE
        }
        else{
            binding.main.visibility = View.VISIBLE
            binding.mainCopy.visibility = View.GONE
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sotAdsConfigurations = SOTAdsManager.getConfigurations()
        Log.i("SOTStartTestActivity", "walkthrough2_scr")
        lifecycleScope.launch {
            val targetImageView = if (scaleType == 0) {
                binding.main.visibility = View.GONE
                binding.mainCopy.visibility = View.VISIBLE
                binding.mainCopy
            } else {
                binding.main.visibility = View.VISIBLE
                binding.mainCopy.visibility = View.GONE
                binding.main
            }

            // Load image into the visible ImageView
            withContext(Dispatchers.Main) {
                Glide.with(requireActivity())
                    .load(item.drawableResId)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .skipMemoryCache(true)
                    .into(targetImageView)
            }
        }
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                Glide.with(requireActivity())
                    .asDrawable()
                    .load(item.drawableBubbleResId)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .skipMemoryCache(true)
                    .into(binding.bubble)
            }
        }

        binding.txtHeading.setTextColor(ContextCompat.getColor(requireContext(),item.headingColor))
        binding.txtDescription.setTextColor(ContextCompat.getColor(requireContext(),item.descriptionColor))
        binding.btnNext.setTextColor(ContextCompat.getColor(requireContext(),item.nextColor))

        binding.txtHeading.text = item.heading
        binding.txtDescription.text = item.description
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(),item.viewBackgroundColor))

        if (!NetworkCheck.isNetworkAvailable(context)) {
            binding.glOne.setGuidelinePercent(0.8f)
        }
        binding.btnNext.setOnClickListener {
            Log.i("SOTStartTestActivity", "walkthrough2_scr_tap_next")
            eventTracker?.logEvent(
                requireActivity(),
                "walkthrough2_scr_tap_next"
            )
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 2
        }

        val nativeWalkThrough3Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_3") as? Boolean ?: false
        if (nativeWalkThrough3Enabled) {
            loadAdmobWTThreeNatives()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!NetworkCheck.isNetworkAvailable(context)) {
            binding.glOne.setGuidelinePercent(0.8f)
            binding.nativeAdContainerAd.visibility = View.GONE
        }

        val nativeWalkThrough1Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_2") as? Boolean ?: false
        if (nativeWalkThrough1Enabled) {
            binding.nativeAdContainerAd.visibility = View.VISIBLE
            showAdmobWTTwoNatives()
        } else {
            binding.nativeAdContainerAd.visibility = View.GONE
        }
    }

    private fun loadAdmobWTThreeNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_3")?.let { adId ->
            AdmobNativeAdManager.requestAd(
                mContext = requireActivity(),
                adId = adId,
                adName = "WALKTHROUGH_3",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } ?: Log.i("WTTwoFragment","ADMOB_NATIVE_WALKTHROUGH_3 ad ID is missing.")
    }


    private fun showAdmobWTTwoNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_2")?.let { adId ->
            AdmobNativeAdManager.requestAd(
                mContext = requireActivity(),
                adId = adId,
                adName = "WALKTHROUGH_2",
                isMedia = true,
                isMediumAd = true,
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_2").toString().toBoolean(),
                populateView = true,
                adContainer = binding.nativeAdContainerAd,
                onAdFailed = {
                    binding.nativeAdContainerAd.visibility = View.GONE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_2: Admob: onAdFailed()")
                },
                onAdLoaded = {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_2: Admob: onAdLoaded()")
                }
            )
        } ?: Log.w("WTOneFragment", "ADMOB_NATIVE_WALKTHROUGH_2 ad ID is missing.")
    }
  
}