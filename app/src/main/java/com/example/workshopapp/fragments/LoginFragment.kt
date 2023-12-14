package com.example.workshopapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.workshopapp.Database.DatabaseHelper
import com.example.workshopapp.R
import com.example.workshopapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var binding : FragmentLoginBinding ?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val manager = parentFragmentManager

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val status = sharedPref!!.getBoolean( getString(R.string.userlogined), false)
        if (status) {
            manager.beginTransaction().replace(R.id.fragView,UserDashboard())
                .commit()
        }

        binding?.goRegisterBtn?.setOnClickListener {
            manager.beginTransaction().replace(R.id.fragView,RegisterFragment())
                .addToBackStack("LoginTORegister").commit()
        }

        binding?.loginBtn?.setOnClickListener {
            checkLogin(manager)
        }

        binding?.skipBtn?.setOnClickListener {
            manager.beginTransaction().addToBackStack("LoginToWorklist")
                .replace(R.id.fragView,WorkshopsListFragment()).commit()
        }

        return binding?.root!!
    }

    private fun checkLogin(fragManager : FragmentManager){
        val email = binding?.etEmail?.text.toString()
        val password = binding?.etPassword?.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(requireContext(),"Fields cannot be empty",
                Toast.LENGTH_SHORT).show()
            return
        }

        val db = DatabaseHelper(requireContext())

        val user = db.loginCheck(email)
        Log.e("User from login", user.toString())

        if(user == null){
            Toast.makeText(requireContext(),"Wrong Username",Toast.LENGTH_SHORT).show()
        }else{
            if(user.password == password){
                Toast.makeText(requireContext(),"Login Success",Toast.LENGTH_SHORT).show()
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                with (sharedPref.edit()) {
                    putBoolean(getString(R.string.userlogined), true)
                    putString(getString(R.string.username), user.username)
                    putString(getString(R.string.useremail), user.email)
                    apply()

                    fragManager.beginTransaction().replace(R.id.fragView,UserDashboard())
                        .commit()
                }
            }else{
                Toast.makeText(requireContext(),"Wrong Password",Toast.LENGTH_SHORT).show()
            }
        }
    }

}