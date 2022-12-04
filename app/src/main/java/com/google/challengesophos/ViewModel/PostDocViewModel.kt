package com.google.challengesophos.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.challengesophos.Repository.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostDocViewModel : ViewModel() {

    //initiate the method when the viewModel is called
    init{
        getCities()
    }

    var citiesLiveData = MutableLiveData<MutableList<String>>()

    var docModel = MutableLiveData<DocItems>()

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun postDoc(DocInput: DocItems) {
        viewModelScope.launch {
            val response = getRetrofit().create(ApiPostDoc::class.java)
                .postDoc(DocInput)

           println("Server response ${response.toString()}")

        }

    }

    //Get the cities to be shown in the spinner of the fragment
    fun getCities() {
        viewModelScope.launch {
            val response = getRetrofit().create(APIGetOffices::class.java)
                .getCities()

            val cities = response.body()?.Items

            //brings the cities available in the API
            if (cities != null) {
                val citiesList = mutableSetOf<String>()
                for (city in cities.indices) {
                    citiesList.add(cities[city].Ciudad)
                }


                citiesLiveData.postValue(citiesList.toMutableList())
                println("I'm the set  $citiesList")


            }
        }

    }

}