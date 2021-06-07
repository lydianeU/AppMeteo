package fr.afpa.appmeteo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WindSpeed(
        @field:Json(name ="speed") val speed: String,
): Parcelable {

    fun returnSpeed(): String {
        return "$speed"
    }

}