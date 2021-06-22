package fr.afpa.appmeteo.model

import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*

class AlarmHour(context: Context) {
    //permet de récupérer l'heure pour l'alarme
    var context:Context=context

    fun readAlarmHour():String?{
        val prefs : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var alarmHour : String? = prefs.getString("horaire", "no Alarm hour")
        return alarmHour


    }
    //permet de récuper heure de l'alarme
    fun getAlarmHour(alarmHour:String):String {

        var index = alarmHour.indexOf(':')
        return alarmHour.substring(0,index)
    }
    //permet de récupérer les minutes de l'alarme
    fun getAlarmMinutes(alarmHour: String):String? {
        var index = alarmHour.indexOf(':')
        return alarmHour.substring(index+1, alarmHour.length)
    }
    //permet de vérifier si l'heure courante est égale à l'heure réglée sur l'alarme
    @RequiresApi(Build.VERSION_CODES.N)
    fun compareWithAlarmTime ():Boolean{

        var calendar = Calendar.getInstance()
        var currentHour = calendar.get(Calendar.HOUR_OF_DAY)+2
        if (currentHour==24)currentHour=0
        if (currentHour==25)currentHour=1
        var currentMinutes = calendar.get(Calendar.MINUTE).toString()

        var alarmHour = readAlarmHour()
        var alarmHoursetted = getAlarmHour(alarmHour as String)
        var alarmMinutesSetted = getAlarmMinutes(alarmHour as String)
        if (currentHour.toString().equals(alarmHoursetted) && currentMinutes.equals(alarmMinutesSetted))
            return true
        else
            return false

    }
    private fun checkIfAlarmSet():Boolean{
        if (readAlarmHour()?.isEmpty() == true)
            return false
        else{
            return true
        }
    }


}