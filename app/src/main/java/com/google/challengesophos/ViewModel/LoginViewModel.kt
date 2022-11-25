package com.google.challengesophos.ViewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.challengesophos.Repository.model.APIogin
import com.google.challengesophos.Repository.model.LoginApiResponse
import com.google.challengesophos.ui.LoginFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//Add a constructor of context as a helper to get a context here in the viewModel
class LoginViewModel() : ViewModel() {

    var loginModel = MutableLiveData<LoginFragment>()


    //variables use to show a message for the fingerprint


    //Method retrofit that is use to call the Api in the next method
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Method that is connected to the view through the parameters and it connects to the model with retrofit.
    fun getLoginViewModel(emailIn: String, passwordIn: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<LoginApiResponse> = getRetrofit().create(APIogin::class.java)
                .getLogin(emailIn, passwordIn)

            val userInfo = response.body()

            if (userInfo?.acceso == true) {
                println(userInfo.nombre)
                //navigation pending

            } else {
                //Fun that makes the Toast from the fragment
                //Toast pending


            }


        }

    }




}
