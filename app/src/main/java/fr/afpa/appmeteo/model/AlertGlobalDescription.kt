package fr.afpa.appmeteo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlertGlobalDescription(
        @field:Json(name ="description") val alertDescription: String
): Parcelable {


    fun returnAlertDescription(): String {
        return "$alertDescription"
    }

}