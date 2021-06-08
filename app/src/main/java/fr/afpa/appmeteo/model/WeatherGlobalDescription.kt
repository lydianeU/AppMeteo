package fr.afpa.appmeteo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherGlobalDescription(
        @field:Json(name ="description") val globalDescription: String
): Parcelable {


    fun returnGlobalDescription(): String {
        return "$globalDescription"
    }

}



