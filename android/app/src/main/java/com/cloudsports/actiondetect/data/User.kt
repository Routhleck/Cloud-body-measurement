package com.cloudsports.actiondetect.data

class User {
    data class LoginRequest(val name: String,val password : String)
    data class LoginResponse(val map: Map<String,Any>)
    data class RegisterRequest(val name: String,val password: String)
}