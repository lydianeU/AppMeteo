package fr.afpa.appmeteo.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class Formatter {

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTimeDisplay(currentTimeST: Long): String? {
        val dt = Instant.ofEpochSecond(currentTimeST).atZone(ZoneId.of("UTC+2")).toLocalDateTime()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formatted = dt.format(formatter)
        return formatted
    }

}