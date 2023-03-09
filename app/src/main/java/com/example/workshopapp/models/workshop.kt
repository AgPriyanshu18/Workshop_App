package com.example.workshopapp.models

import java.io.Serializable

data class workshop(
    val workshopId : Int,
    val workshopName : String,
    val date : String,
    val workshopDesc : String,
    val fees : Int
) : Serializable