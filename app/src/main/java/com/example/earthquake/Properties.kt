package com.example.earthquake
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Properties(
    val mag : Double,
    val title : String,
    val place : String,
    val time : Int,
    val url : String
) : Parcelable
