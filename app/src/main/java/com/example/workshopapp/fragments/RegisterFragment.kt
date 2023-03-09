package com.example.workshopapp.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.workshopapp.Database.DatabaseHelper
import com.example.workshopapp.databinding.FragmentRegisterBinding
import com.example.workshopapp.models.user

class RegisterFragment : Fragment() {
    var binding : FragmentRegisterBinding ?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        val manager = parentFragmentManager
        binding!!.registerBtn.setOnClickListener {
            registerUser(manager)
        }
        return binding!!.root
    }

    fun registerUser(manager : FragmentManager){

        val email = binding?.etEmail?.text.toString()
        val username = binding?.etUserName?.text.toString()
        val pass = binding?.etPassword?.text.toString()

        if(email.isNullOrEmpty() || username.isNullOrEmpty() || pass.isNullOrEmpty()){
            Toast.makeText(requireContext(),"Fields cannot be empty",
                Toast.LENGTH_SHORT).show()
            return
        }

        val user = user(email,username,pass)

        val db = DatabaseHelper(requireContext())

        val addUser = db.addUser(user)
        if (addUser > 0){
            Toast.makeText(requireContext(),"Registration Success" ,
                Toast.LENGTH_SHORT).show()
            manager.popBackStack()
        }else{
            Toast.makeText(requireContext(),"Please Try Again" ,
                Toast.LENGTH_SHORT).show()
        }
    }

}