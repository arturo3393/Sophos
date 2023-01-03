package com.google.challengesophos.repository.model

data class DocResponse(
    var Items: List<DocItems>,
    var Count: Int,
    var ScannedCount: Int
)
