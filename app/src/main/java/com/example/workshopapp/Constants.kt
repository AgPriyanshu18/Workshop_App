package com.example.workshopapp

import com.example.workshopapp.models.workshop

object Constants {

    fun getWorkShops() : ArrayList<workshop> {
        val allWorkshops : ArrayList<workshop> = ArrayList()

        val que1 = workshop(
            1, "WorkShop1",
            "Date1", "High level description one",
            2000
        )

        allWorkshops.add(que1)

        val que2 = workshop(
            2, "WorkShop2",
            "Date2", "High level description two",
            4000
        )

        allWorkshops.add(que2)

        val que3 = workshop(
            3, "WorkShop3",
            "Date3", "High level description three",
            8000
        )

        allWorkshops.add(que3)

        val que4 = workshop(
            4, "WorkShop4",
            "Date4", "High level description four",
            7000
        )

        allWorkshops.add(que4)

        val que5 = workshop(
            5, "WorkShop5",
            "Date5", "High level description five",
            10000
        )

        allWorkshops.add(que5)

        return allWorkshops
    }


}