package com.google.challengesophos.viewModel


import androidx.lifecycle.*
import com.google.challengesophos.repository.model.APILogin
import com.google.challengesophos.repository.model.LoginApiResponse
import kotlinx.coroutines.*
import retrofit2.Response



class LoginViewModel() : ViewModel() {

    private val _loginResponse: MutableLiveData<Response<LoginApiResponse>> = MutableLiveData()
    val loginApiResponse: LiveData<Response<LoginApiResponse>>
        get() = _loginResponse


    //Method that is connected to the view through the parameters and it connects to the model with retrofit.
    fun getLoginViewModel(emailIn: String, passwordIn: String) =
        viewModelScope.launch {
            _loginResponse.value =
                RetrofitHelper.getRetrofit().create(APILogin::class.java)
                    .getLogin(emailIn, passwordIn)
        }


    fun cleanLiveData (){
        _loginResponse.value?.body()?.acceso = false
    }

}



