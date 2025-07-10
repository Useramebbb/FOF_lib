-keep class android.webkit.** { *; }
-dontwarn android.webkit.**
-dontwarn com.facebook.infer.annotation.Nullsafe$Mode
-dontwarn com.facebook.infer.annotation.Nullsafe

-keep class com.facebook.infer.annotation.** { *; }
-dontwarn com.facebook.infer.annotation.**

-keep public class androidx.viewpager.widget.PagerAdapter{*;}
#-keep public class androidx.viewpager.widget.ViewPager.OnPageChangeListener{*;}
-keep interface androidx.annotation.IntDef{*;}
-keep interface androidx.annotation.Nullable{*;}
-keep interface androidx.annotation.CheckResult{*;}
-keep interface androidx.annotation.NonNull{*;}
-keep public class androidx.fragment.app.Fragment{*;}
-keep public class androidx.core.content.FileProvider{*;}
-keep public class androidx.core.app.NotificationCompat{*;}
-keep public class androidx.appcompat.widget.AppCompatImageView {*;}
-keep public class androidx.recyclerview.*{*;}

-keep class com.manual.mediation.library.foflib.activities.AppCompatBaseActivity.** { *; }
-keep class com.manual.mediation.library.foflib.activities.LanguageScreenOne.** { *; }
-keep class com.manual.mediation.library.foflib.activities.LanguageScreenDup.** { *; }
-keep class com.manual.mediation.library.foflib.activities.WalkThroughConfigActivity.** { *; }
-keep class com.manual.mediation.library.foflib.activities.WelcomeScreenOne.** { *; }
-keep class com.manual.mediation.library.foflib.activities.WelcomeScreenDup.** { *; }
-keep class com.manual.mediation.library.foflib.activities.WTFullScreenAdFragment.** { *; }
-keep class com.manual.mediation.library.foflib.activities.WTOneFragment.** { *; }
-keep class com.manual.mediation.library.foflib.activities.WTThreeFragment.** { *; }
-keep class com.manual.mediation.library.foflib.activities.WTTwoFragment.** { *; }

-keep class com.manual.mediation.library.foflib.adapters.LanguageAdapter.** { *; }
-keep class com.manual.mediation.library.foflib.adapters.WalkThroughAdapter.** { *; }

-keep class com.manual.mediation.library.foflib.adMobAdClasses.AdMobBannerAdSplash.** { *; }
-keep class com.manual.mediation.library.foflib.adMobAdClasses.AdmobInterstitialAdSplash.** { *; }
-keep class com.manual.mediation.library.foflib.adMobAdClasses.AdmobNativeAdManager.** { *; }
-keep class com.manual.mediation.library.foflib.adMobAdClasses.AdmobResumeAdSplash.** { *; }

-keep class com.manual.mediation.library.foflib.callingClasses.LanguageScreensConfiguration.** { *; }
-keep class com.manual.mediation.library.foflib.callingClasses.SOTAdsConfigurations.** { *; }
-keep class com.manual.mediation.library.foflib.callingClasses.SOTAdsManager.** { *; }
-keep class com.manual.mediation.library.foflib.callingClasses.WalkThroughScreensConfiguration.** { *; }
-keep class com.manual.mediation.library.foflib.callingClasses.WelcomeScreensConfiguration.** { *; }

-keep class com.manual.mediation.library.foflib.data.Language.** { *; }
-keep class com.manual.mediation.library.foflib.data.WalkThroughItem.** { *; }

-keep interface com.manual.mediation.library.foflib.interfaces.LanguageInterface.** { *; }
-keep interface com.manual.mediation.library.foflib.interfaces.WelcomeInterface.** { *; }
-keep interface com.manual.mediation.library.foflib.interfaces.OnNextButtonClickListener.** { *; }


-keep class com.manual.mediation.library.foflib.utils.AdLoadingDialog.** { *; }
-keep class com.manual.mediation.library.foflib.utils.ExFunKt.** { *; }
-keep class com.manual.mediation.library.foflib.utils.MyLocaleHelper.** { *; }
-keep class com.manual.mediation.library.foflib.utils.NetworkCheck.** { *; }
-keep class com.manual.mediation.library.foflib.utils.PrefHelper.** { *; }

-keep class com.manual.mediation.library.foflib.utilsGoogleAdsConsent.ConsentConfigurations.** { *; }
-keep class com.manual.mediation.library.foflib.utilsGoogleAdsConsent.GoogleMobileAdsConsentManager.** { *; }