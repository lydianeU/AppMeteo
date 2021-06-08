package fr.afpa.appmeteo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Coordinates(
        @field:Json(name ="lon") val longitude: String,
        @field:Json(name ="lat") val latitude: String
): Parcelable {

    fun returnLongitude(): String {
        return "$longitude"
    }

    fun returnLatitude(): String {
        return "$latitude"
    }

}