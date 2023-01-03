package com.google.challengesophos.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.challengesophos.repository.model.APIGetDocById
import com.google.challengesophos.repository.model.DocItems
import com.google.challengesophos.repository.model.DocResponse
import kotlinx.coroutines.launch
import retrofit2.Response


class GetDocsByIdViewModel: ViewModel() {

    var getDocsImgMutableLiveData = MutableLiveData<List<DocItems>>()

//it gets the specific data of a document by its ID
    fun getDocsViewModel (idDoc:String) {
        viewModelScope.launch {
            val response : Response<DocResponse> = RetrofitHelper.getRetrofit().create(APIGetDocById::class.java)
                .getSpecificDoc(idDoc)

            val docsInfo = response.body()

            getDocsImgMutableLiveData.postValue(docsInfo?.Items)

            }

}


}