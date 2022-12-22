package com.google.challengesophos.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.challengesophos.Repository.model.*
import kotlinx.coroutines.launch


class PostDocViewModel : ViewModel() {

    //initiate the method when the viewModel is called
    init {
        getCities()
    }

    var citiesLiveData = MutableLiveData<MutableList<String>>()

    var docModel = MutableLiveData<DocItems>()


    fun postDoc(DocInput: DocItemsPost) {
        viewModelScope.launch {
            RetrofitHelper.getRetrofit().create(ApiPostDoc::class.java)
                .postDoc(DocInput)

        }

    }

    //Get the cities to be shown in the spinner of the fragment
    fun getCities() {
        viewModelScope.launch {
            val response = RetrofitHelper.getRetrofit().create(APIGetOffices::class.java)
                .getCities()

            val cities = response.body()?.Items

            //brings the cities available in the API
            if (cities != null) {
                val citiesList = mutableSetOf<String>()
                for (city in cities.indices) {
                    citiesList.add(cities[city].Ciudad)
                }


                citiesLiveData.postValue(citiesList.toMutableList())


            }
        }

    }

}