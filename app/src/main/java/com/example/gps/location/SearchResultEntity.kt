package com.example.gps.location

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

//@Parcelize
data class SearchResultEntity(
    val fullAddress: String,
    val name: String,
//    val locationLatLng: LocationLatLngEntity
    val locationLatLng: LatLng
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(LatLng::class.java.classLoader)!!) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<SearchResultEntity> {
        override fun createFromParcel(parcel: Parcel): SearchResultEntity {
            return SearchResultEntity(parcel)
        }

        override fun newArray(size: Int): Array<SearchResultEntity?> {
            return arrayOfNulls(size)
        }
    }
}
