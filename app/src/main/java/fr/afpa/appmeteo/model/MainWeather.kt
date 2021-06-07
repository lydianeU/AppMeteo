package fr.afpa.appmeteo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainWeather(
        @field:Json(name ="temp") val temperature: String,
        @field:Json(name ="feels_like") val experiencedTemperature: String,
        @field:Json(name ="humidity") val humidity: String
): Parcelable {

    fun returnTemperature(): String {
        return "$temperature"
    }

    fun returnExperiencedTemperature(): String {
        return "$experiencedTemperature"
    }

    fun returnHumidity(): String {
        return "$humidity"
    }

}
