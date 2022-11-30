package com.google.challengesophos.ui

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.util.PatternsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.challengesophos.R
import com.google.challengesophos.databinding.FragmentLoginBinding

import com.google.challengesophos.ViewModel.LoginViewModel
import java.util.concurrent.Executor


class LoginFragment : Fragment(R.layout.fragment_login) {

    private val loginViewModel: LoginViewModel by viewModels()

    //biometrics
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo

    private var _binding: FragmentLoginBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //it removes the name of the app in the action bar
        (requireActivity() as AppCompatActivity).supportActionBar?.title = " "

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



        //Login button that allows or denies the access to the app
        binding.btnLogin.setOnClickListener {

            val emailIn = binding.etEmail.text?.trim().toString() ?: ""
            val passwordIn = binding.etPassword.text?.trim().toString() ?: ""
            validateEmail(emailIn)
            loginViewModel.getLoginViewModel(emailIn, passwordIn)
            println("Value of the loginModel: " + loginViewModel.loginModel.value)
            when (loginViewModel.loginModel.value) {
                true -> navigateToWelcomeFragment()
                else -> toastLogin()
            }

        }
        //Method that checks if the device has biometrics
        checkDeviceHasBiometric()

        // It checks the fingerprint authentificaiton
        binding.btnFingerprint.setOnClickListener {
            fingerPrintAuthentification()
            biometricPrompt.authenticate(promptInfo)
        }






        return binding.root

    }


    private fun toastLogin() {
        Toast.makeText(
            context,
            "The email or password entered is invalid",
            Toast.LENGTH_SHORT
        ).show()
    }

    //sends the user to the other screen with argument of the user name to the next fragment
    fun navigateToWelcomeFragment() {

        view?.findNavController()
            ?.navigate(LoginFragmentDirections.actionLoginFragmentToWelcomeFragment(loginViewModel.userNameLiveData.value))
        println("Here we have the userName " + loginViewModel.userNameLiveData.value)
    }

    //it allows or denies the access with fingerprint
    private fun fingerPrintAuthentification() {
        executor = ContextCompat.getMainExecutor(requireContext())

        biometricPrompt = androidx.biometric.BiometricPrompt(this, executor,
            object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        context,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }


                override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        context,
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()

                    navigateToWelcomeFragment()

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        context, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            })

        promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login Sophos")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

    }

    //check if the device can use biometrics, if not, it send it to the device configuration
    private fun checkDeviceHasBiometric() {
        val biometricManager = androidx.biometric.BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MiniReto2", "App can authenticate using biometrics.")
                binding.tvMsg.text = "App can authenticate using biometrics."
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("MiniReto2", "No biometric features available on this device.")
                binding.tvMsg.text = "No biometric features available on this device."
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d("MY_APP_TAG", "Biometric features are currently unavailable.")
                binding.tvMsg.text = "Biometric features are currently unavailable."
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or
                                BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                }
                startActivityForResult(enrollIntent, 100)
            }

        }


    }

    //It checks if the person has entered an email
    private fun validateEmail(email: String) {
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Field must be an email"

        }
    }
 //it hides the toolbar
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
//it shows the toolbar
    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }


}