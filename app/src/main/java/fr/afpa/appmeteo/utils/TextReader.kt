package fr.afpa.appmeteo.utils

import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi

class TextReader(speaker: Speaker) {

    var textToRead = ""
    var speaker: Speaker = speaker


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun read(text: String){
        textToRead = text
        readText()
        Handler().postDelayed(this::closeTextReader, 5000000)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun readText() {
        speaker?.speak(textToRead)

    }

    private fun closeTextReader() {
        speaker?.onDestroy()
    }
}