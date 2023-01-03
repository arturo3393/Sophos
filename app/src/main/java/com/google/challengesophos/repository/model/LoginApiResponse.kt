package com.google.challengesophos.repository.model


data class LoginApiResponse(
  var id: String,
  var nombre: String,
  var apellido: String,
  var acceso: Boolean,
  var admin: Boolean
)
