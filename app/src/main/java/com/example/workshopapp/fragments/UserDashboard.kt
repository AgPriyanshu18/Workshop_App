package com.example.workshopapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workshopapp.Adapter.workshopListAdapter
import com.example.workshopapp.Database.DatabaseHelper
import com.example.workshopapp.R
import com.example.workshopapp.databinding.FragmentUserDashboardBinding
import com.example.workshopapp.models.workshop

class UserDashboard : Fragment() {
   private var binding : FragmentUserDashboardBinding ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDashboardBinding.inflate(layoutInflater)
        setUpUi()

        binding?.exploreBtn?.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.fragView , WorkshopsListFragment()
            ).addToBackStack("dashboardToWorkshoplist").commit()
        }

        binding?.logOut?.setOnClickListener {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            with(sharedPref!!.edit()) {
                putBoolean(getString(R.string.userlogined), false)
                putString(getString(R.string.username), null)
                putString(getString(R.string.useremail), null)
                apply()
            }
            parentFragmentManager.beginTransaction().replace(
                R.id.fragView,LoginFragment()
            ).commit()
        }

        return binding?.root
    }

    private fun setUpUi() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val name = sharedPref?.getString( getString(R.string.username), "")
        val email = sharedPref?.getString( getString(R.string.useremail), "")
        binding?.dashboardWelcome?.text = "Welcome ${name}"
        val db = DatabaseHelper(requireContext())
        val wkUserList = db.getWkListPfUser(email!!)
        val wkList = db.getWkList()
        val userWorkshopList : ArrayList<workshop> = ArrayList()
        for (workshop in wkList){
            for (id in wkUserList){
                if (id == workshop.workshopId){
                    userWorkshopList.add(workshop)
                }
            }
        }
        val adapter = workshopListAdapter(userWorkshopList , true)
        binding?.userDashboardRV?.adapter = adapter
        binding?.userDashboardRV?.layoutManager = LinearLayoutManager(requireContext())
    }

}