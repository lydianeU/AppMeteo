package fr.afpa.appmeteo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ForecastWeather(
        @field:Json(name = "daily") val dailyWeather: List<DailyWeather>
) : Parcelable {

}