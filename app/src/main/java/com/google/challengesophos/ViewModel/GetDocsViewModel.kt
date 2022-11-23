package com.google.challengesophos.ViewModel

import androidx.lifecycle.ViewModel
import com.google.challengesophos.Repository.model.APIServiceGetDocs
import com.google.challengesophos.Repository.model.DocResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetDocsViewModel : ViewModel(){

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getDocsViewModel (emailLogin:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response : Response<DocResponse> = getRetrofit().create(APIServiceGetDocs::class.java)
                .getDocs(emailLogin)

            val docsInfo = response.body()

            println(response.isSuccessful)

            docsInfo?.Items?.forEach{
                println(it)
            }

        }
    }
}