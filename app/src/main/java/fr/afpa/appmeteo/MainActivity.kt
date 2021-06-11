package fr.afpa.appmeteo

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import fr.afpa.appmeteo.model.CurrentWeather
import fr.afpa.appmeteo.rest.ClientOpenWeather
import fr.afpa.appmeteo.utils.Formatter
import fr.afpa.appmeteo.utils.Speaker
import fr.afpa.appmeteo.utils.TextReader
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class MainActivity : AppCompatActivity() {

    var clientOpenWeather = ClientOpenWeather()
    var formatter = Formatter()
    var speaker : Speaker? = null
    lateinit var textReader : TextReader


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        speaker = Speaker(this)
        try {
            textReader = TextReader(speaker!!)
        } catch(e:NullPointerException)
        {
            Log.e("UTILS","la variable speaker ne doit pas etre null")
        }

        buttonRechercher.setOnClickListener { getCurrentWeather()  }

    }

    private fun getCurrentWeather() {

        var cityName = editTextCityName.text.toString()
        var errorText = ""
        clientOpenWeather.serviceApi.getCurrentWeather(cityName).enqueue(object : Callback<CurrentWeather> {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherCode = response.code()
                val weatherResponse = response.body()
                if (!response.isSuccessful){
                    when ( weatherCode){

                        500 -> errorText="Serveur non disponible"
                        404 -> errorText= "La ville n'a pas été reconnue, veuillez recommencer"
                        401 -> errorText = "Erreur d'authentification, Veuillez contacter le support"
                        400 -> errorText = "Veuillez entrer une ville"
                        else -> errorText = "Veuillez recommencer ou contacter le support"
                    }
                    displayUserMessage(errorText)
                }
                else {

                    weatherResponse?.let {
                        displayWeather(weatherResponse)
                    }
                }
               /* if (weatherCode == 404) {
                    displayUserMessage()
                } else {

                    weatherResponse?.let {
                        displayWeather(weatherResponse)
                    }*/

            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e("CURRENT", "Error : $t")

            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun displayUserMessage(errorText:String) {
        //changer le message en toast
        textViewWeather.text = errorText
        if (switchReader.isChecked) {
            textReader.read(textViewWeather.text.toString())
        }


        /*
        val text:CharSequence = "Ville non reconnue"
        val duration = Toast.LENGTH_SHORT
        Toast.makeText(this,text, duration).show()
        */

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayWeather(weatherResponse: CurrentWeather) {

        var currentTimeST = weatherResponse.returnCurrentWeatherTime().toLong()
        val formattedTime = formatter.formatTimeDisplay(currentTimeST)

        ("Actuellement à " + weatherResponse.returnCityName() + " \n" +
                "(Dernière mise à jour à  $formattedTime )\n" +
                "Météo globale : ${weatherResponse.weatherGlobalDescription.get(0).returnGlobalDescription()}.\n" +
                "Il fait ${weatherResponse.mainWeather.returnTemperature()}°C " +
                "avec une température ressentie de ${weatherResponse.mainWeather.returnExperiencedTemperature()}°C \n" +
                "Vitesse du vent : ${weatherResponse.windSpeed.returnSpeed()}m/s \n" +
                "Ciel couvert à ${weatherResponse.cloudiness.returnCloudPercentage()}%\n" +
                "Données récupérées pour le deuxième appel API : longitude = ${weatherResponse.coordinates.returnLongitude()} "+
                "et latitude = ${weatherResponse.coordinates.returnLatitude()}"
                ).also { textViewWeather.text = it }

        if (switchReader.isChecked) {
            textReader.read(textViewWeather.text.toString())
        }

    }





}