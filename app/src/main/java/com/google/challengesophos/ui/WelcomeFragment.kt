package com.google.challengesophos.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.challengesophos.R
import com.google.challengesophos.ViewModel.LoginViewModel
import com.google.challengesophos.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null

    //loginViewModel used to bring the user's name
    private val loginViewModel: LoginViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

/*
        //sets the toolbar in the fragment but it doesn't show the menu
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbarWelcome)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
*/
        //it brings the name of the user by the safe args of the navigation saved in the bundle
        //and it is put in the action bar
        if (arguments?.getString("user_name") == null) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = ""
        } else {
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                arguments?.getString("user_name").toString()
        }

        //set the text color of the toolbar
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //activate the option Menu
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //binding initialized
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)




        binding.btnSendDocs.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_welcomeFragment_to_sendDocsFragment)

        }

        binding.btnSeeDocs.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_welcomeFragment_to_seeDocsFragment)
        }

        binding.btnOffices.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_welcomeFragment_to_officesFragment)

        }



        return binding.root
    }


    //used to activate the optionsMenu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //takes user to the destination in the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (val id = item.itemId) {
            R.id.sendDocsMenu -> view?.findNavController()
                ?.navigate(R.id.action_welcomeFragment_to_sendDocsFragment)
            R.id.seeDocsMenu -> view?.findNavController()
                ?.navigate(R.id.action_welcomeFragment_to_seeDocsFragment)
            R.id.officesMenu -> view?.findNavController()
                ?.navigate(R.id.action_welcomeFragment_to_officesFragment)
            //missing the dark and language menu
            /*R.id.darkModeMenu->view?.findNavController()?.navigate(R
                    R.id.languageMenu->view?.findNavController()?.navigate(R*/

        }

        return super.onOptionsItemSelected(item)
    }


}