package com.google.challengesophos.repository.model

data class OfficeResponse(
    var Items : List<OfficeItems>,
    var Count : Int,
    var ScannedCount: Int
)
