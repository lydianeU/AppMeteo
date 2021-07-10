package fr.afpa.appmeteo.rest

import fr.afpa.appmeteo.BuildConfig
import fr.afpa.appmeteo.model.CurrentWeather
import fr.afpa.appmeteo.model.ForecastWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IOpenWeather {



    companion object {
        val rootAPI = "https://api.openweathermap.org/data/2.5/"
    }

       @GET("weather")
       fun getCurrentWeather(@Query("q") cityName:String,
                             @Query("units") units:String ="metric",
                             @Query("appid") appid:String = BuildConfig.API_KEY,
                             @Query("lang") lang:String ="fr" ): Call<CurrentWeather>


       @GET("onecall")
       fun getForecastWeather(
               @Query("lat") latitude:String,
               @Query("lon") longitude:String,
               @Query("units") units:String ="metric",
               @Query("appid") appid:String =BuildConfig.API_KEY,
               @Query("lang") lang:String ="fr"
       ) : Call<ForecastWeather>

}

