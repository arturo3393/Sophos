package com.google.challengesophos.Repository.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APILogin {

    // Api that returns the loginResponse Model
    @GET("/RS_Usuarios")
    suspend fun getLogin(
        @Query("idUsuario") userID: String,
        @Query("clave") password: String
    ): Response<LoginApiResponse>


}