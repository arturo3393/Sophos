package com.google.challengesophos.Repository.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIGetDocById {

    @GET("/RS_Documentos")
    suspend fun getSpecificDoc(
        @Query("idRegistro") idRegister:String
    ): Response<DocResponse>
}