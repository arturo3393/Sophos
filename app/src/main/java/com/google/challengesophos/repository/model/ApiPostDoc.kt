package com.google.challengesophos.repository.model

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiPostDoc {
//This sends the corresponding info to post a doc
    @POST("/RS_Documentos")
    suspend fun postDoc(
        @Body docInfo: DocItemsPost
    )
}