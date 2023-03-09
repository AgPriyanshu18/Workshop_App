package com.example.workshopapp.models

import java.io.Serializable

data class user(
    val email : String,
    val username : String,
    val password : String
) : Serializable