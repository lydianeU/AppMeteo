package fr.afpa.appmeteo.rest

import fr.afpa.appmeteo.model.CurrentWeather
import retrofit2.Call
import retrofit2.http.GET

interface IOpenWeather {

    companion object {
        val ENDPOINT = "https://api.openweathermap.org/data/2.5/"
    }

       @GET("weather?q=lyon&appid=f50171b37d12220c536279035b9e7db9")
       fun getCurrentWeather(): Call<CurrentWeather>
}