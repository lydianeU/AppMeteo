package fr.afpa.appmeteo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentWeather(
        val id: String,
        @field:Json(name ="name") val name: String,
        @field:Json(name ="main") val mainWeather: MainWeather,
        @field:Json(name ="wind") val windSpeed: WindSpeed,
        @field:Json(name ="clouds") val cloudiness: Cloudiness
        ): Parcelable {

    fun returnCityName(): String {
        return "$name"
    }
}

