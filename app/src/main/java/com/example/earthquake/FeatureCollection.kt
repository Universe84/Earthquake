package com.example.earthquake

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class FeatureCollection(
    val type: String
) : Parcelable
