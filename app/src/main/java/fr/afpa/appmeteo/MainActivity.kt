package fr.afpa.appmeteo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
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

    var cityName : String = ""
    var defaultCitySet : Boolean = false
    var message : String =""

    @RequiresApi(Build.VERSION_CODES.N)
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

        //vérifie dans les settings de l'application si la personne a rempli une ville par défaut
        defaultCitySet = checkIfDefaultCitySet()

        //personnaliser un message d'accueil selon s'il y a userName ou pas dans les settings

        if (checkIfDefaultUserName()) {
            message = "BONJOUR ${readUserName()}"
            textView_bonjour.text = message
        }

        buttonRechercher.setOnClickListener {

            getCurrentWeather()

        }

        //permet de stopper le reader si switchReader est off
        switchReader.setOnClickListener{
            if (!switchReader.isChecked)
            {textReader.speaker.onStop()}
        }


    }


    private fun getCurrentWeather() {

        //si la valeur n'a pas été récupérée dans les settings, alors elle est récupérée dans le champ
        if (!defaultCitySet)
            cityName = editTextCityName.text.toString()

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
                    buttonForecastDisplay.visibility = View.INVISIBLE
                }
                else {

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

        var textToDisplay : String = "Prévisions du jour : \n\n" +
                "Temperature moyenne de la journée :  ${weatherResponse.dailyWeather.get(0).temperature.returnTempAverageDay()}°C \n\n" +
                "Temperature minimale : ${weatherResponse.dailyWeather.get(0).temperature.returnTempMin()}°C  \n\n" +
                "Temperature maximale : ${weatherResponse.dailyWeather.get(0).temperature.returnTempMax()}°C \n\n" +
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
        textViewWeather.text = ""

        if (switchReader.isChecked) {
            textReader.read(errorText)
        }

        Toast.makeText(applicationContext,errorText, Toast.LENGTH_LONG).show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayWeather(weatherResponse: CurrentWeather) {

        var currentTimeST = weatherResponse.returnCurrentWeatherTime().toLong()
        val formattedTime = formatter.formatTimeDisplay(currentTimeST)

        latitude = weatherResponse.coordinates.returnLatitude()
        longitude = weatherResponse.coordinates.returnLongitude()

                ("Actuellement à " + weatherResponse.returnCityName() +
                " il fait ${weatherResponse.mainWeather.returnTemperature()}°C " +
                "avec une température ressentie de ${weatherResponse.mainWeather.returnExperiencedTemperature()}°C \n\n" +
                "Vitesse du vent : ${weatherResponse.windSpeed.returnSpeed()}m/s \n\n" +
                "Météo globale: ${weatherResponse.weatherGlobalDescription.get(0).returnGlobalDescription()}.\n" +
                "Ciel couvert à ${weatherResponse.cloudiness.returnCloudPercentage()}%\n\n"+
                "(Dernière mise à jour à  $formattedTime )\n"
                ).also { textViewWeather.text = it }

        if (switchReader.isChecked) {
            textReader.read(textViewWeather.text.toString())
        }

    }

    //permet d'accéder aux Settings de l'application
    fun buttonSettings_onClick(view: View) {

        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    //permet de récupérer la valeur de la ville initialisee par défaut dans les settings
    fun readDefaultCityName():String?{
        val prefs : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var defaultCityName : String? = prefs.getString("edit_text_defaultCity", "no default city name")
        return defaultCityName
    }

    private fun checkIfDefaultCitySet(): Boolean {
        val prefs : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var chekDefaultCityName = prefs.getBoolean("switch_defaultCity",false)
        if (chekDefaultCityName){
            if (readDefaultCityName()?.isEmpty() == true){
                 return false}
            else {
              cityName = readDefaultCityName().toString()
              editTextCityName.hint = "Ville par défaut : $cityName"
                editTextCityName.setEnabled(false)
                return true
            }
        }else
            return false

    }

    //permet de récupérer la valeur nom de l'utilisateur
    private fun readUserName():String?{
        val prefs : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var userName : String? = prefs.getString("signature", "no default name")
        return userName
    }
    fun checkIfDefaultUserName(): Boolean {
        if (readUserName()?.isEmpty() == true)
            return false
        else {
            return true
        }
    }


}