package fr.afpa.appmeteo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DailyWeather(
        @field:Json(name = "temp") val temperature: Temperature,
        @field:Json(name = "weather") val weatherGlobalDescription: List<WeatherGlobalDescription>,
        @field:Json(name = "alerts") val alertGlobalDescription: List<AlertGlobalDescription>
) : Parcelable {


}