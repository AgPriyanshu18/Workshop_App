package com.example.workshopapp.models

import java.io.Serializable

data class attended(
    val email : String,
    val workshopId : Int
) : Serializable
