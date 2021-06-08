package fr.afpa.appmeteo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import fr.afpa.appmeteo.model.CurrentWeather
import fr.afpa.appmeteo.rest.ClientOpenWeather
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    private fun displayWeather(weatherResponse: CurrentWeather) {

        textViewWeather.text = "Actuellement à " + weatherResponse.returnCityName() +" à " +
                weatherResponse.returnCurrentWeatherTime() + " :\n" +
                "Météo globale : " + weatherResponse.weatherGlobalDescription.get(0).returnGlobalDescription()+  ".\n" +
                "Il fait " + weatherResponse.mainWeather.returnTemperature() + " °C " +
                "avec une température ressentie de " + weatherResponse.mainWeather.returnExperiencedTemperature()+ " °C \n" +
                "Vitesse du vent : " + weatherResponse.windSpeed.returnSpeed() + " m/s \n" +
                "Ciel couvert à  " + weatherResponse.cloudiness.returnCloudPercentage() + " %\n"

    }
}