package fr.afpa.appmeteo

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import fr.afpa.appmeteo.model.CurrentWeather
import fr.afpa.appmeteo.rest.ClientOpenWeather
import fr.afpa.appmeteo.utils.Formatter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var clientOpenWeather = ClientOpenWeather()
    var formatter = Formatter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonRechercher.setOnClickListener { getCurrentWeather() }

    }

    private fun getCurrentWeather() {

        var cityName = editTextCityName.text.toString()

        clientOpenWeather.serviceApi.getCurrentWeather(cityName).enqueue(object : Callback<CurrentWeather> {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherCode = response.code()
                val weatherResponse = response.body()

                if (weatherCode == 404) {
                    displayUserMessage()
                } else {

                    weatherResponse?.let {
                        displayWeather(weatherResponse)
                    }
                }
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e("CURRENT", "Error : $t")

            }
        })

    }

    private fun displayUserMessage() {
        //changer le message en toast
        textViewWeather.text = "La ville n'a pas été reconnue, veuillez recommencer"
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

    }


}