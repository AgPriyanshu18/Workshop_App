package com.example.workshopapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.workshopapp.databinding.ActivityMainBinding
import com.example.workshopapp.fragments.LoginFragment

class MainActivity : AppCompatActivity() {

    var binding : ActivityMainBinding ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        supportFragmentManager.beginTransaction().replace(R.id.fragView,LoginFragment()).commit()
    }
}

