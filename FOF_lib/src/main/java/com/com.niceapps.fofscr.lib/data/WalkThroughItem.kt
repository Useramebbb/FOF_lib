package com.niceapps.fofscr.lib.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WalkThroughItem(
    val heading: String,
    val description: String,
    val headingColor: Int,
    val descriptionColor: Int,
    val nextColor: Int,
    val drawableResId: Int,
    val drawableBubbleResId: Int,
    val viewBackgroundColor:Int,
    val imageScale:Int
) : Parcelable