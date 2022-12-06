package com.google.challengesophos.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.challengesophos.Repository.model.APIGetDocById
import com.google.challengesophos.Repository.model.APIGetDocs
import com.google.challengesophos.Repository.model.DocItems
import com.google.challengesophos.Repository.model.DocResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetDocsViewModel : ViewModel() {


    var getDocsModelLiveData = MutableLiveData<List<DocItems>>()



    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getDocsList(emailLogin: String) {
        viewModelScope.launch {
            val response: Response<DocResponse> = getRetrofit().create(APIGetDocs::class.java)
                .getDocs(emailLogin)

            val docsInfo = response.body()

            getDocsModelLiveData.postValue(docsInfo?.Items)

            //println(docsInfo?.Items?.toString()) //Doc normal list

            //it avoids the coroutine to continue updating itself
            viewModelScope.cancel()


        }
    }


}
/*
fun getDocsImgByID(idDoc: String) {
    CoroutineScope(Dispatchers.IO).launch {
        val response: Response<DocResponse> = getRetrofit().create(APIGetDocById::class.java)
            .getSpecificDoc(idDoc)

        val docsInfo = response.body()

        println(response.isSuccessful)

        //  println(docsInfo?.Items?.toString()) // Doc detail list

        getDocsImgMutableLiveData.postValue(docsInfo?.Items)

    }
}
/*fun getDocsSpecficImage(docID : String){
viewModelScope.launch {
    val response: Response<DocResponse> = getRetrofit().create(APIGetDocById::class.java)
        .getSpecificDoc(docID)

    val specficDocInfo = response.body()

    getDocsIdMutableLiveData.postValue(specficDocInfo?.Items?.get(0)?.Adjunto)
    println("In the viewModel"+  getDocsIdMutableLiveData.postValue(specficDocInfo?.Items?.get(0)?.Adjunto) )
}

}*/