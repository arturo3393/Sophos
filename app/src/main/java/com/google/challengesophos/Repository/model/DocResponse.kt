package com.google.challengesophos.Repository.model

data class DocResponse(
    var Items: List<DocItems>,
    var Count: Int,
    var ScannedCount: Int
)
