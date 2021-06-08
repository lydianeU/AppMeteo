package fr.afpa.appmeteo

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import fr.afpa.appmeteo.model.CurrentWeather
import fr.afpa.appmeteo.rest.ClientOpenWeather
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.ZoneId
import java.time.*

class MainActivity : AppCompatActivity() {

    var clientOpenWeather = ClientOpenWeather()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonRechercher.setOnClickListener{getCurrentWeather()}

    }

    private fun getCurrentWeather() {

        var cityName = editTextCityName.text.toString()

        clientOpenWeather.serviceApi.getCurrentWeather(cityName).enqueue(object : Callback<CurrentWeather> {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherResponse = response.body()

                weatherResponse?.let {
                    displayWeather(weatherResponse)
                }
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e("REG", "Error : $t")

            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayWeather(weatherResponse: CurrentWeather) {



        var currentTimeST = weatherResponse.returnCurrentWeatherTime().toLong()
        val dt = Instant.ofEpochSecond(currentTimeST).atZone(ZoneId.systemDefault()).toLocalDateTime()
        //val dt = Instant.ofEpochSecond(currentTimeST).atZone(ZoneId.of("UTC"))

        ("Actuellement à " + weatherResponse.returnCityName() +" à $dt " + " :\n" +
        "Météo globale : " + weatherResponse.weatherGlobalDescription.get(0).returnGlobalDescription()+  ".\n" +
        "Il fait " + weatherResponse.mainWeather.returnTemperature() + " °C " +
        "avec une température ressentie de " + weatherResponse.mainWeather.returnExperiencedTemperature()+ " °C \n" +
        "Vitesse du vent : " + weatherResponse.windSpeed.returnSpeed() + " m/s \n" +
        "Ciel couvert à  " + weatherResponse.cloudiness.returnCloudPercentage() + " %\n"+
        "Données récupérées pour le deuxième appel API : longitude = " + weatherResponse.coordinates.returnLongitude()+
        " et latitude = "+ weatherResponse.coordinates.returnLatitude()
                ).also { textViewWeather.text = it }

    }
}