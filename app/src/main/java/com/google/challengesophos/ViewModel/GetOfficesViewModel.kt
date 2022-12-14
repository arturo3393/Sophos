package com.google.challengesophos.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.challengesophos.Repository.model.APIGetOffices
import com.google.challengesophos.Repository.model.OfficeItems
import com.google.challengesophos.Repository.model.OfficeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response


class GetOfficesViewModel : ViewModel() {

    init {
        getOffices()
    }

    val citiesLiveData = MutableLiveData<List<OfficeItems>>()

    fun getOffices (){
        viewModelScope.launch {
            val response: Response<OfficeResponse> =RetrofitHelper.getRetrofit().create(APIGetOffices::class.java)
                .getCities()

            val citiesInfo = response.body()

            citiesLiveData.postValue(citiesInfo?.Items)

            viewModelScope.cancel()

        }
    }

    //it gets the specific city, it will be used for future development
    fun getOfficesViewModel (city:String) {
        CoroutineScope(Dispatchers.IO).launch {

            val response : Response<OfficeResponse> = RetrofitHelper.getRetrofit().create(APIGetOffices::class.java)
                .getOffices(city)


            val cityInfo = response.body()

            cityInfo?.Items?.forEach{
                println(it.toString())
            }

        }
    }
}