package com.google.challengesophos.ViewModel


import androidx.lifecycle.*
import com.google.challengesophos.Repository.model.APIGetOffices
import com.google.challengesophos.Repository.model.APIogin
import com.google.challengesophos.Repository.model.LoginApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginViewModel() : ViewModel() {


    var loginModel = MutableLiveData<Boolean>(false) //validates the login
    var userNameLiveData = MutableLiveData<String?>() //gets the user name

    //Method retrofit that is use to call the Api in the next method
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Method that is connected to the view through the parameters and it connects to the model with retrofit.
    fun getLoginViewModel(emailIn: String, passwordIn: String) {
        viewModelScope.launch {
            val response: Response<LoginApiResponse> = getRetrofit().create(APIogin::class.java)
                .getLogin(emailIn, passwordIn)

            val userInfo = response.body()
            val userName = userInfo?.nombre

            if (userInfo?.acceso == true) {
                //println("true")
                loginModel.postValue(true)
                userNameLiveData.postValue(userName)
            } else {
                //println("false")
                loginModel.postValue(false)
            }
        }

    }


}
