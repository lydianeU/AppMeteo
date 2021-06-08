package fr.afpa.appmeteo.rest

import fr.afpa.appmeteo.model.CurrentWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IOpenWeather {

    companion object {
        val rootAPI = "https://api.openweathermap.org/data/2.5/"
    }

       @GET("weather")
       fun getCurrentWeather(@Query("q") cityName:String,
                             @Query("units") units:String ="metric",
                             @Query("appid") appid:String ="f50171b37d12220c536279035b9e7db9",
                             @Query("lang") lang:String ="fr" ): Call<CurrentWeather>
}

