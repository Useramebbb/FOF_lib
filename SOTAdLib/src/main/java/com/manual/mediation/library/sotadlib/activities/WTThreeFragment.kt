package com.manual.mediation.library.sotadlib.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdMobInterstitialInside
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.data.WalkThroughItem
import com.manual.mediation.library.sotadlib.databinding.FragmentWTThreeBinding
import com.manual.mediation.library.sotadlib.interfaces.CommonEventTracker
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import com.manual.mediation.library.sotadlib.utils.PrefHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WTThreeFragment : Fragment() {
    private var _binding: FragmentWTThreeBinding? = null
    private val binding get() = _binding!!
    private var sotAdsConfigurations: SOTAdsConfigurations? = null
    private lateinit var item: WalkThroughItem
    private var eventTracker: CommonEventTracker? = null
    private var adShown = false
    private var scaleType :Int = 0
    companion object {
        private const val ARG_ITEM = "walkThroughItem"

        fun newInstance(item: WalkThroughItem, tracker: CommonEventTracker? = null): WTThreeFragment {
            return WTThreeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ITEM, item)
                }
                eventTracker = tracker
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<WalkThroughItem>(ARG_ITEM)?.let {
            item = it
            scaleType = item.imageScale
        } ?: throw IllegalStateException("WalkThroughItem must be provided")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWTThreeBinding.inflate(inflater, container, false)
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
        eventTracker?.logEvent(requireActivity(), "walkthrough3_scr")

        Log.i("SOTStartTestActivity", "walkthrough3_scr")

        loadImages()
        setupTextViews()
        setupButton()
    }

    private fun loadImages() {
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                context?.let {

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


                    Glide.with(it)
                        .load(item.drawableBubbleResId)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .skipMemoryCache(true)
                        .into(binding.bubble)
                }
            }
        }
    }

    private fun setupTextViews() {
        context?.let {
            binding.txtHeading.setTextColor(ContextCompat.getColor(requireContext(),item.headingColor))
            binding.txtDescription.setTextColor(ContextCompat.getColor(requireContext(),item.descriptionColor))
            binding.btnNext.setTextColor(ContextCompat.getColor(requireContext(),item.nextColor))
            binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(),item.viewBackgroundColor))
        }

        binding.txtHeading.text = item.heading
        binding.txtDescription.text = item.description
    }

    private fun setupButton() {
        binding.btnNext.setOnClickListener {
            eventTracker?.logEvent(requireActivity(), "walkthrough3_scr_tap_start")
            if (sotAdsConfigurations?.getRemoteConfigData()?.get("INTERSTITIAL_LETS_START") as? Boolean == true) {
                safeShowAdmobWTThreeInterstitial()
            } else {
                safeLetsStartClick()
            }
        }
    }

    private fun safeShowAdmobWTThreeInterstitial() {
        if (!isAdded || activity == null) return

        viewLifecycleOwner.lifecycleScope.launch {
            AdMobInterstitialInside.showIfAvailableOrLoadAdMobInterstitial(
                context = requireActivity(),
                nameFragment = "WALKTHROUGH_3",
                adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_INTERSTITIAL_LETS_START") ?: "",
                onAdClosedCallBackAdmob = {
                    Log.i("SOT_ADS_TAG", "Interstitial: WALKTHROUGH_3: onAdClosedCallBackAdmob()")
                   // delay(300)
                    if (isAdded) {
                        safeLetsStartClick()
                    }
                },
                onAdShowedCallBackAdmob = {
                    Log.i("SOT_ADS_TAG", "Interstitial: WALKTHROUGH_3: onAdShowedCallBackAdmob()")
                }
            )
        }
    }

    private fun safeLetsStartClick() {
        if (!isAdded || activity == null) return

        Log.i("SOTStartTestActivity", "walkthrough3_scr_tap_start")
        PrefHelper(requireActivity()).putBoolean("StartScreens", value = true)
        SOTAdsManager.notifyFlowFinished()
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()
        if (!isAdded) return

        if (!NetworkCheck.isNetworkAvailable(context)) {
            binding.glOne.setGuidelinePercent(0.8f)
            binding.nativeAdContainerAd.visibility = View.GONE
            return
        }

        if (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_3") as? Boolean == true) {
            safeShowAdmobWTThreeNatives()
        } else {
            binding.nativeAdContainerAd.visibility = View.GONE
        }
    }

    private fun safeShowAdmobWTThreeNatives() {
        if (!isAdded || activity == null) return

        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_3")?.let { adId ->
            AdmobNativeAdManager.requestAd(
                mContext = requireActivity(),
                adId = adId,
                adName = "WALKTHROUGH_3",
                isMedia = true,
                isMediumAd = true,
                remoteConfig = sotAdsConfigurations
                    ?.getRemoteConfigData()
                    ?.getValue("NATIVE_WALKTHROUGH_3")
                    .toString()
                    .toBoolean(),
                populateView = true,
                adContainer = binding.nativeAdContainerAd,
                onAdFailed = {
                    if (isAdded) {
                        binding.nativeAdContainerAd.visibility = View.GONE
                    }
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_3: Admob: onAdFailed()")
                },
                onAdLoaded = {
                    if (isAdded) {
                        binding.nativeAdContainerAd.visibility = View.VISIBLE
                    }
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_3: Admob: onAdLoaded()")
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}