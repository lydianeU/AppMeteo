package fr.afpa.appmeteo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cloudiness(
        @field:Json(name ="all") val cloudPercentage: String,
): Parcelable {

    fun returnCloudPercentage(): String {
        return "$cloudPercentage"
    }

}