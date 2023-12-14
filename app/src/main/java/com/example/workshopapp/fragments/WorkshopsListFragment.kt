package com.example.workshopapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workshopapp.Adapter.workshopListAdapter
import com.example.workshopapp.Database.DatabaseHelper
import com.example.workshopapp.R
import com.example.workshopapp.databinding.FragmentWorkshopsListBinding
import com.example.workshopapp.models.attended
import com.example.workshopapp.models.workshop

class WorkshopsListFragment : Fragment() {
    var binding : FragmentWorkshopsListBinding ?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkshopsListBinding.inflate(layoutInflater)
        setupRV()

        binding?.userDashBtn?.setOnClickListener {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            val status = sharedPref!!.getBoolean( getString(R.string.userlogined), false)
            if (status){
                parentFragmentManager.beginTransaction().replace(
                    R.id.fragView , UserDashboard()
                ).commit()
            }else{
                Toast.makeText(requireContext(),"Login First",Toast.LENGTH_SHORT).show()
            }
        }

        binding?.backBtn?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding?.root
    }

    fun setupRV(){
        val db = DatabaseHelper(requireContext())

        val wkList = db.getWkList()

        val adapter = workshopListAdapter(wkList , false)

        adapter.setOnClickListener(object  : workshopListAdapter.OnClickListener{
            override fun onClick(position: Int, workshop: workshop) {
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                val highScore = sharedPref.getBoolean( getString(R.string.userlogined), false)
                if (highScore){
                    val db = DatabaseHelper(requireContext())
                    val email = sharedPref.getString(getString(R.string.useremail),"")
                    val result = db.addWorkshopUser(attended(email!!, workshop.workshopId))
                    parentFragmentManager.popBackStack()
                    Toast.makeText(requireContext(),
                    "You are attending ${workshop.workshopName}",
                    Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),
                    "You need to Login First",
                    Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            }

        })

        binding?.workshopRV?.adapter = adapter
        binding?.workshopRV?.layoutManager = LinearLayoutManager(requireContext())
    }

}