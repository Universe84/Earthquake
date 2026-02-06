package com.example.earthquake
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Feature(
    val properties : Properties,
    val geometry: Geometry
) : Parcelable

