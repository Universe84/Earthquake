package com.example.earthquake
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Metadata(
    val title: String,
    val count: Int,
    val status: Int
) : Parcelable
