package com.google.challengesophos.Repository.model


data class LoginApiResponse(
  var id: String,
  var nombre: String,
  var apellido: String,
  var acceso: Boolean,
  var admin: Boolean
)
