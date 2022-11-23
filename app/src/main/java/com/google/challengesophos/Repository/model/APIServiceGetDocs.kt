package com.google.challengesophos.Repository.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServiceGetDocs {
    @GET("/RS_Documentos")
    suspend fun getDocs(
        @Query("correo") email:String
    ): Response<DocResponse>
}