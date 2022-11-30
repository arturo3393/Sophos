package com.google.challengesophos.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.challengesophos.R
import com.google.challengesophos.databinding.FragmentSendDocsBinding

class SendDocsFragment : Fragment(R.layout.fragment_send_docs) {

    private var _binding: FragmentSendDocsBinding? = null


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
        _binding = FragmentSendDocsBinding.inflate(inflater, container, false)




        //using the id of the include in the layout the back arrow takes the user
        //back to the previous fragment (welcome)
        binding.custumToolbar.ivArrowBackSendDocs.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_sendDocsFragment_to_welcomeFragment)
        }

        return binding.root

    }


}