package fr.afpa.appmeteo.utils

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresApi
import java.util.*


class Speaker(context: Context?) : TextToSpeech.OnInitListener {

    private val tts: TextToSpeech
    private var ready = false

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.FRANCE
            ready = true
        } else {
            ready = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun speak(text: String?) {
        if (ready) {
            val utteranceId = UUID.randomUUID().toString()
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId)

        }
    }

    fun onDestroy() {
        tts.shutdown()
    }
}