package com.google.challengesophos.repository.model

import retrofit2.Response
import retrofit2.http.GET


interface APIGetOffices {
    /*
//This brings the complete list of offices in a city

    @GET("RS_Oficinas")
    suspend fun getOffices(
        @Query("ciudad") city:String
    ): Response<OfficeResponse>
*/

    //Bring the complete items list of the offices, it is used to get the list of cities
    @GET("RS_Oficinas")
    suspend fun getCities()
    : Response<OfficeResponse>

}