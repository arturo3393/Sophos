package com.google.challengesophos.ViewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.challengesophos.Repository.model.APIServiceLogin
import com.google.challengesophos.Repository.model.LoginApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginViewModel : ViewModel() {

    var loginModel = MutableLiveData<LoginApiResponse>()

    //Method retrofit that is use to call the Api in the next method
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Method that is connected to the view through the parameters and it connects to the model with retrofit.
    fun getLoginViewModel (emailLogin:String, passwordLogin: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response : Response<LoginApiResponse> = getRetrofit().create(APIServiceLogin::class.java)
                .getLogin(emailLogin,passwordLogin)

            val userNFO = response.body()
            println(response.isSuccessful)
                println(userNFO?.nombre.toString())

        }
    }

    }