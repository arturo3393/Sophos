package com.google.challengesophos.Repository.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIGetOffices {
//This brings the complete list of offices in a city
    @GET("RS_Oficinas")
    suspend fun getOffices(
        @Query("ciudad") city:String
    ): Response<OfficeResponse>

    //Bring the complete items list of the offices, it is used to get the list of cities
    @GET("RS_Oficinas")
    suspend fun getCities()
    : Response<OfficeResponse>

}