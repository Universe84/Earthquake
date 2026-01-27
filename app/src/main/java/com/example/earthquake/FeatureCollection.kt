package com.example.earthquake

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class FeatureCollection(
    val metadata : Metadata,
    val features: List<Feature>
) : Parcelable
