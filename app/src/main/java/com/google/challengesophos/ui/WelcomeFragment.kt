package com.google.challengesophos.ui

import android.os.Bundle
import android.view.*
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.challengesophos.R
import com.google.challengesophos.ViewModel.LoginViewModel
import com.google.challengesophos.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)






    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //binding initialized
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)


        //puts the name to the appbar
        (activity as AppCompatActivity).supportActionBar?.title = arguments?.getString("user_name")
        //disables the back array
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)



        binding.btnSendDocs.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_welcomeFragment_to_sendDocsFragment)

        }

        binding.btnSeeDocs.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_welcomeFragment_to_seeDocsFragment)
        }

        binding.btnOffices.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_welcomeFragment_to_officesFragment)

        }



        //Bring the user's name with safe args
        showUsersName()



        return binding.root
    }

    fun showUsersName(){
         if(arguments?.getString("user_name")?.isNotEmpty() == true){
             (activity as AppCompatActivity).supportActionBar?.title = arguments?.getString("user_name")
         } else{
             (activity as AppCompatActivity).supportActionBar?.title = "Pablo"
         }
    }



}