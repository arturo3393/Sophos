package com.google.challengesophos.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.challengesophos.Repository.model.APIGetDocById
import com.google.challengesophos.Repository.model.APIGetDocs
import com.google.challengesophos.Repository.model.DocItems
import com.google.challengesophos.Repository.model.DocResponse
import com.google.challengesophos.ui.SeeDocsFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class GetDocsViewModel : ViewModel(){


      var getDocsModelLiveData = MutableLiveData<List<DocItems>>()

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getDocsList (emailLogin:String) {
        viewModelScope.launch {
            val response : Response<DocResponse> = getRetrofit().create(APIGetDocs::class.java)
                .getDocs(emailLogin)

            val docsInfo = response.body()

            getDocsModelLiveData.postValue(docsInfo?.Items)

            //it avoids the coroutine to continue updating itself
            viewModelScope.cancel()

           /* docsInfo?.Items?.forEach{
                getDocsModelLiveData.postValue(it)
                println(it)
            }*/

        }
    }

    /*
    fun getDocsSpecficImage(docID : String){
        viewModelScope.launch {
            val response: Response<DocItems> = getRetrofit().create(APIGetDocById::class.java)
                .getSpecificDoc(docID)

            val specficDocInfo = response.body()
            val image = specficDocInfo?.Adjunto
        }

    }*/
}