package com.google.challengesophos.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.challengesophos.repository.model.APIGetDocs
import com.google.challengesophos.repository.model.DocItems
import com.google.challengesophos.repository.model.DocResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response


class GetDocsViewModel : ViewModel() {


    val getDocsModelLiveData = MutableLiveData<List<DocItems>>()



//it gets the complete list of focuments with the user's email
    fun getDocsList(emailLogin: String) {
        viewModelScope.launch {
            val response: Response<DocResponse> = RetrofitHelper.getRetrofit().create(APIGetDocs::class.java)
                .getDocs(emailLogin)

            val docsInfo = response.body()

            getDocsModelLiveData.postValue(docsInfo?.Items)

            //println(docsInfo?.Items?.toString()) //Doc normal list

            //it avoids the coroutine to continue updating itself
            viewModelScope.cancel()


        }
    }


}