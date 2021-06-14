package fr.afpa.appmeteo

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import fr.afpa.appmeteo.model.CurrentWeather
import fr.afpa.appmeteo.model.ForecastWeather
import fr.afpa.appmeteo.rest.ClientOpenWeather
import fr.afpa.appmeteo.utils.Formatter
import fr.afpa.appmeteo.utils.Speaker
import fr.afpa.appmeteo.utils.TextReader
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var clientOpenWeather = ClientOpenWeather()
    var formatter = Formatter()
    var speaker : Speaker? = null
    lateinit var textReader : TextReader
    var latitude : String = ""
    var longitude: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        speaker = Speaker(this)
        buttonForecastDisplay.visibility = View.INVISIBLE
        try {
            textReader = TextReader(speaker!!)
        } catch(e:NullPointerException)
        {
            Log.e("UTILS","la variable speaker ne doit pas etre null")
        }

        buttonRechercher.setOnClickListener { getCurrentWeather() }

    }

    private fun getCurrentWeather() {

        var cityName = editTextCityName.text.toString()
        clientOpenWeather.serviceApi.getCurrentWeather(cityName).enqueue(object : Callback<CurrentWeather> {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherCode = response.code()
                val weatherResponse = response.body()
                if (!response.isSuccessful){
                    val errorText = when ( weatherCode){
                        500 -> "Serveur non disponible"
                        404 -> "La ville n'a pas été reconnue, veuillez recommencer"
                        401 -> "Erreur d'authentification, Veuillez contacter le support"
                        400 -> "Veuillez entrer une ville"
                        else -> "Veuillez recommencer ou contacter le support"
                    }
                    displayUserMessage(errorText)
                }
                else {
                    //displayCurrentWeather

                    weatherResponse?.let {
                        displayWeather(weatherResponse)
                    }
                    buttonForecastDisplay.visibility = View.VISIBLE
                    //appel 2ème API
                    buttonForecastDisplay.setOnClickListener {
                        getForecastWeather(latitude, longitude)
                        buttonForecastDisplay.visibility = View.INVISIBLE
                    }


                }

            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e("CURRENT", "Error : $t")

            }
        })

    }

    private fun getForecastWeather(latitude: String, longitude: String) {


        clientOpenWeather.serviceApi.getForecastWeather(latitude, longitude).enqueue(object : Callback<ForecastWeather> {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<ForecastWeather>, response: Response<ForecastWeather>) {

                val weatherResponse = response.body()


                    weatherResponse?.let {
                        displayForecastWeather(weatherResponse)
                    }


                }

            override fun onFailure(call: Call<ForecastWeather>, t: Throwable) {
                Log.e("CURRENT", "Error : $t")
            }

        })

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun displayForecastWeather(weatherResponse: ForecastWeather) {

        var textToDisplay : String = "PRÉVISIONS DU JOUR : \n" +
                "Temperature moyenne de la journée :  ${weatherResponse.dailyWeather.get(0).temperature.returnTempAverageDay()}°C \n" +
                "Temperature minimale : ${weatherResponse.dailyWeather.get(0).temperature.returnTempMin()}°C  \n" +
                "Temperature maximale : ${weatherResponse.dailyWeather.get(0).temperature.returnTempMax()}°C \n" +
                "Description météo du jour : ${weatherResponse.dailyWeather.get(0).weatherGlobalDescription.get(0).returnGlobalDescription()} \n"
        if (!(weatherResponse.dailyWeather.get(0).alertGlobalDescription.isNullOrEmpty())) {
            textToDisplay.plus("\n Alerte : ${weatherResponse.dailyWeather.get(0).alertGlobalDescription.get(0).returnAlertDescription()}")
                }

        textViewWeather.text = textToDisplay
        if (switchReader.isChecked) {
            textReader.read(textViewWeather.text.toString())
        }

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

        latitude = weatherResponse.coordinates.returnLatitude()
        longitude = weatherResponse.coordinates.returnLongitude()

        ("Actuellement à " + weatherResponse.returnCityName() + " \n" +
                "(Dernière mise à jour à  $formattedTime )\n" +
                "Météo globale : ${weatherResponse.weatherGlobalDescription.get(0).returnGlobalDescription()}.\n" +
                "Il fait ${weatherResponse.mainWeather.returnTemperature()}°C " +
                "avec une température ressentie de ${weatherResponse.mainWeather.returnExperiencedTemperature()}°C \n" +
                "Vitesse du vent : ${weatherResponse.windSpeed.returnSpeed()}m/s \n" +
                "Ciel couvert à ${weatherResponse.cloudiness.returnCloudPercentage()}%\n"
                ).also { textViewWeather.text = it }

        if (switchReader.isChecked) {
            textReader.read(textViewWeather.text.toString())
        }

    }





}