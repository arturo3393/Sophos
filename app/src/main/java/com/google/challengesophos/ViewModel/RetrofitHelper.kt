package com.google.challengesophos.ViewModel

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//object used to make the API requests in the whole app
object RetrofitHelper {
    fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}