package com.google.challengesophos.ViewModel



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.challengesophos.Repository.model.APIogin
import com.google.challengesophos.Repository.model.LoginApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class LoginViewModel() : ViewModel() {

    var loginModel = MutableLiveData<Boolean>(true)





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

            when (userInfo?.acceso == false ){
                true ->loginModel.postValue(true)
                else->  loginModel.postValue(false)


            }

            /*

            if (userInfo?.acceso == true) {
                //navigation validation to go to the next fragment (Welcome fragment)
                //if the loginModel remains false a Toast shows up
               loginModel.
             } else{
                loginModel.postValue(true)
            }*/
        }

    }




}
