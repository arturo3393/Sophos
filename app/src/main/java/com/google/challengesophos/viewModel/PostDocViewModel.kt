package com.google.challengesophos.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.challengesophos.repository.model.*
import kotlinx.coroutines.launch


class PostDocViewModel : ViewModel() {

    //initiate the method when the viewModel is called
    init {
        getCities()
    }

    private var _citiesLiveData = MutableLiveData<MutableList<String>>()
    val citiesLiveData: LiveData<MutableList<String>>
    get() = _citiesLiveData


    val docModel = MutableLiveData<DocItems>()


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


                _citiesLiveData.postValue(citiesList.toMutableList())


            }
        }

    }

}