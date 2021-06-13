package fr.afpa.appmeteo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Temperature(
        @field:Json(name = "day") val tempAverageDay: String,
        @field:Json(name = "min") val tempMin: String,
        @field:Json(name = "max") val tempMax: String,
) : Parcelable {

    fun returnTempAverageDay(): String {
        return "$tempAverageDay"
    }

    fun returnTempMin(): String {
        return "$tempMin"
    }

    fun returnTempMax(): String {
        return "$tempMax"
    }




}