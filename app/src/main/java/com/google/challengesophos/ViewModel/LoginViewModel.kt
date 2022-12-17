package com.google.challengesophos.ViewModel


import androidx.lifecycle.*
import com.google.challengesophos.Repository.model.APIogin
import com.google.challengesophos.Repository.model.LoginApiResponse
import kotlinx.coroutines.*
import retrofit2.Response



class LoginViewModel() : ViewModel() {

    val _loginResponse: MutableLiveData<Response<LoginApiResponse>> = MutableLiveData()
    val loginApiResponse: LiveData<Response<LoginApiResponse>>
        get() = _loginResponse


    //Method that is connected to the view through the parameters and it connects to the model with retrofit.
    fun getLoginViewModel(emailIn: String, passwordIn: String) {
        viewModelScope.launch {
            _loginResponse.value =
                RetrofitHelper.getRetrofit().create(APIogin::class.java)
                    .getLogin(emailIn, passwordIn)

        }

    }
}



