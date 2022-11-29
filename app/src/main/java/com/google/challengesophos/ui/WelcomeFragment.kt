package com.google.challengesophos.ui

import android.content.ClipData.Item
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.challengesophos.R
import com.google.challengesophos.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {

   private var _binding: FragmentWelcomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //activate the option Menu
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //binding initialized
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)



        return binding.root
    }

    //eeded to activate the optionsMenu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //takes user to the destination in the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (val id = item.itemId){
            R.id.sendDocsMenu->view?.findNavController()?.navigate(R.id.action_welcomeFragment_to_sendDocsFragment)
            R.id.seeDocsMenu ->view?.findNavController()?.navigate(R.id.action_welcomeFragment_to_seeDocsFragment)
            R.id.officesMenu->view?.findNavController()?.navigate(R.id.action_welcomeFragment_to_officesFragment)
            //missing the dark and language menu
            /*R.id.darkModeMenu->view?.findNavController()?.navigate(R
                    R.id.languageMenu->view?.findNavController()?.navigate(R*/

        }

        binding.cvSendDocs.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_welcomeFragment_to_sendDocsFragment)
                println("I'm touching")
        }

        binding.btnSendDocs.setOnClickListener {

        }

        binding.btnOffices.setOnClickListener {

        }


        return super.onOptionsItemSelected(item)
    }



    fun navigateToNextFragment(id: Int) {
        view?.findNavController()?.navigate(id)
    }

}