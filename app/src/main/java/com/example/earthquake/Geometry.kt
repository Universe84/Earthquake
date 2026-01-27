package com.example.earthquake
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Geometry(
    val coordinates : List<Int>
) : Parcelable
