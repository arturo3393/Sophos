package com.google.challengesophos.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.challengesophos.Repository.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostDocViewModel : ViewModel() {

    var docModel = MutableLiveData<DocItems>()

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun postDoc(Docinput:DocItems ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = getRetrofit().create(ApiPostDoc::class.java)
                .postDoc(Docinput)
            // Pending to know the type of response or how I knot that it posted the info
        }

    }

}