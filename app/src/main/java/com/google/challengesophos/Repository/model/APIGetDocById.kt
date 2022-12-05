package com.google.challengesophos.Repository.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIGetDocById {
//This brings the detailed info from a single document by its ID
    @GET("/RS_Documentos")
    suspend fun getSpecificDoc(
        @Query("idRegistro") idRegister:String
    ): Response<DocItems>
}