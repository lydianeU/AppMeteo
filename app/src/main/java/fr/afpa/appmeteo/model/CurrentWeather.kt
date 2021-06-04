package fr.afpa.appmeteo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentWeather(val id: String, val name: String): Parcelable {

    override fun toString(): String {
        return "$name"
    }
}
