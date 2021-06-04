package fr.afpa.appmeteo.rest

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ClientOpenWeather {

    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(IOpenWeather.ENDPOINT).addConverterFactory(MoshiConverterFactory.create())
            .build()
    val serviceApi = retrofit.create(IOpenWeather::class.java)
}