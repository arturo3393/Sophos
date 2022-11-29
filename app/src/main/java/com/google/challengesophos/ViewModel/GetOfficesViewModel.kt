package com.google.challengesophos.ViewModel

import androidx.lifecycle.ViewModel
import com.google.challengesophos.Repository.model.APIGetOffices
import com.google.challengesophos.Repository.model.OfficeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetOfficesViewModel : ViewModel() {

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getOfficesViewModel (city:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response : Response<OfficeResponse> = getRetrofit().create(APIGetOffices::class.java)
                .getOffices(city)

            val cityInfo = response.body()

            println(response.isSuccessful)

            cityInfo?.Items?.forEach{
                println(it.toString())
            }

        }
    }
}