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

-keep class com.manual.niceapps.fofscr.lib.activities.AppCompatBaseActivity.** { *; }
-keep class com.manual.niceapps.fofscr.lib.activities.LanguageScreenOne.** { *; }
-keep class com.manual.niceapps.fofscr.lib.activities.LanguageScreenDup.** { *; }
-keep class com.manual.niceapps.fofscr.lib.activities.WalkThroughConfigActivity.** { *; }
-keep class com.manual.niceapps.fofscr.lib.activities.WelcomeScreenOne.** { *; }
-keep class com.manual.niceapps.fofscr.lib.activities.WelcomeScreenDup.** { *; }
-keep class com.manual.niceapps.fofscr.lib.activities.WTFullScreenAdFragment.** { *; }
-keep class com.manual.niceapps.fofscr.lib.activities.WTOneFragment.** { *; }
-keep class com.manual.niceapps.fofscr.lib.activities.WTThreeFragment.** { *; }
-keep class com.manual.niceapps.fofscr.lib.activities.WTTwoFragment.** { *; }

-keep class com.niceapps.fofscr.lib.adapters.LanguageAdapter.** { *; }
-keep class com.niceapps.fofscr.lib.adapters.WalkThroughAdapter.** { *; }

-keep class com.niceapps.fofscr.lib.adMobAdClasses.AdMobBannerAdSplash.** { *; }
-keep class com.niceapps.fofscr.lib.adMobAdClasses.AdmobInterstitialAdSplash.** { *; }
-keep class com.niceapps.fofscr.lib.adMobAdClasses.AdmobNativeAdManager.** { *; }
-keep class com.niceapps.fofscr.lib.adMobAdClasses.AdmobResumeAdSplash.** { *; }

-keep class com.niceapps.fofscr.lib.callingClasses.LanguageScreensConfiguration.** { *; }
-keep class com.niceapps.fofscr.lib.callingClasses.FOFAdsConfigurations.** { *; }
-keep class com.niceapps.fofscr.lib.callingClasses.FOFAdsManager.** { *; }
-keep class com.niceapps.fofscr.lib.callingClasses.WalkThroughScreensConfiguration.** { *; }
-keep class com.niceapps.fofscr.lib.callingClasses.WelcomeScreensConfiguration.** { *; }

-keep class com.niceapps.fofscr.lib.data.Language.** { *; }
-keep class com.niceapps.fofscr.lib.data.WalkThroughItem.** { *; }

-keep interface com.niceapps.fofscr.lib.interfaces.LanguageInterface.** { *; }
-keep interface com.niceapps.fofscr.lib.interfaces.WelcomeInterface.** { *; }
-keep interface com.niceapps.fofscr.lib.interfaces.OnNextButtonClickListener.** { *; }


-keep class com.niceapps.fofscr.lib.utils.AdLoadingDialog.** { *; }
-keep class com.niceapps.fofscr.lib.utils.ExFunKt.** { *; }
-keep class com.niceapps.fofscr.lib.utils.MyLocaleHelper.** { *; }
-keep class com.niceapps.fofscr.lib.utils.NetworkCheck.** { *; }
-keep class com.niceapps.fofscr.lib.utils.PrefHelper.** { *; }

-keep class com.niceapps.fofscr.lib.utilsGoogleAdsConsent.ConsentConfigurations.** { *; }
-keep class com.niceapps.fofscr.lib.utilsGoogleAdsConsent.GoogleMobileAdsConsentManager.** { *; }