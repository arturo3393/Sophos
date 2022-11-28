package com.google.challengesophos.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.challengesophos.R
import com.google.challengesophos.ViewModel.LoginViewModel
import com.google.challengesophos.databinding.FragmentLoginBinding


class LoginFragment : Fragment(R.layout.fragment_login) {

    private val loginViewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null


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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        //Observer to put the Toast  function, the same for navigation

        loginViewModel.loginModel.observe(viewLifecycleOwner, Observer {
            val emailIn = binding.etEmail.text?.trim().toString() ?: ""
           val passwordIn = binding.etPassword.text?.trim().toString() ?: ""
            loginViewModel.getLoginViewModel(emailIn, passwordIn)

        })

        binding.btnLogin.setOnClickListener {

            val emailIn = binding.etEmail.text?.trim().toString() ?: ""
            val passwordIn = binding.etPassword.text?.trim().toString() ?: ""
            loginViewModel.getLoginViewModel(emailIn, passwordIn)
            println("Value of the loginModel: " + loginViewModel.loginModel.value)
            when (loginViewModel.loginModel.value) {
                true -> navigateToWelcomeFragment()
                else -> toastLogin()
            }


        }

        binding.btnFingerprint.setOnClickListener {

        }

        //05ftK5Ly0J9s

        return binding.root

    }


    fun toastLogin() {
        Toast.makeText(
            context,
            "The email or password entered is invalid",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun navigateToWelcomeFragment() {
        view?.findNavController()?.navigate(R.id.action_loginFragment_to_welcomeFragment)
    }



}