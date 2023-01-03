package com.google.challengesophos.ui


import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.util.PatternsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.challengesophos.R
import com.google.challengesophos.databinding.FragmentLoginBinding
import com.google.challengesophos.viewModel.LoginViewModel
import java.util.concurrent.Executor


class LoginFragment : Fragment(R.layout.fragment_login) {

    private val loginViewModel: LoginViewModel by viewModels()

    private var userName:String? = null

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

        //Method that checks if the device has biometrics
        checkDeviceHasBiometric()

        //Login button that allows or denies the access to the app
        binding.btnLogin.setOnClickListener {
            val emailIn = binding.etEmail.text?.trim().toString()
            val passwordIn = binding.etPassword.text?.trim().toString()

            validateEmail(emailIn)
            loginViewModel.getLoginViewModel(emailIn, passwordIn)

            binding.progressBarLogin.visibility = View.VISIBLE

            //it validates the API response
            loginViewModel.loginApiResponse.observe(viewLifecycleOwner, Observer {
                if(it.body()?.acceso == true){
                    binding.btnLogin.isEnabled = false //it prevents users to make multiple clicks while request is working
                    saveFingerSharedPreferences()
                    userName = it.body()!!.nombre
                    navigateToWelcomeFragment()

                } else{
                    binding.progressBarLogin.visibility = View.GONE
                    toastLogin()

                }
            })


        }



        // It checks the fingerprint authentificaiton
        binding.btnFingerprint.setOnClickListener {

            if (!establishPrintSharedPreferences()) {
                establishPrintSharedPreferences()
            } else {

                fingerPrintAuthentification()
                biometricPrompt.authenticate(promptInfo)

            }
        }


        //TO DELETE!!! It allows me to put my email and password
         binding.etEmail.setText("arturo3393@gmail.com")
         binding.etPassword.setText("05ftK5Ly0J9s")


        return binding.root

    }


    private fun toastLogin() {
        Toast.makeText(
            context,
            R.string.login_error,
            Toast.LENGTH_SHORT
        ).show()
    }

    //sends the user to the other screen with argument of the user name to the next fragment
    fun navigateToWelcomeFragment() {

        view?.findNavController()
            ?.navigate(
                LoginFragmentDirections.actionLoginFragmentToWelcomeFragment(
                    userName,
                    binding.etEmail.text?.trim().toString()
                )
            )

        loginViewModel.cleanLiveData()

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
                        R.string.auth_succeeded, Toast.LENGTH_SHORT
                    )
                        .show()

                    loadUserFingerPrintPreferences()

                    val emailIn = binding.etEmail.text?.trim().toString()
                    val passwordIn = binding.etPassword.text?.trim().toString()
                    loginViewModel.getLoginViewModel(emailIn, passwordIn)

                    //it validates the API response
                    loginViewModel.loginApiResponse.observe(viewLifecycleOwner, Observer {
                        if(it.body()?.acceso == true){
                            userName = it.body()!!.nombre
                            navigateToWelcomeFragment()
                        } else{
                            toastLogin()
                        }
                    })

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        context, R.string.auth_failed,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            })

        // String for the promptInfo
        val biometricTitle = getString(R.string.biometric_tittle)
        val biometricSubtitle = getString(R.string.biometric_subtitle)
        val biometricNegative = getString(R.string.biometric_negative)
        promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle(biometricTitle)
            .setSubtitle(biometricSubtitle)
            .setNegativeButtonText(biometricNegative)
            .build()

    }

    //check if the device can use biometrics, if not, it send it to the device configuration
    private fun checkDeviceHasBiometric() {
        //Strings for the messages
        val biomanagerSuccess = getString(R.string.biomanager_success)
        val biomanagerErrorHardware = getString(R.string.biomanager_error_hardware)
        val biomanagerErrorHW = getString(R.string.biomanaer_error_hw)

        val biometricManager = androidx.biometric.BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("Sophos", "App can authenticate using biometrics.")
                binding.tvMsg.text = biomanagerSuccess
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("Sophos", "No biometric features available on this device.")
                binding.tvMsg.text = biomanagerErrorHardware
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d("Sophos", "Biometric features are currently unavailable.")
                binding.tvMsg.text = biomanagerErrorHW
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
        //String for the message
        val validateEmailMsg = getString(R.string.validate_email)
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = validateEmailMsg

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

    //establishes the function that lets the user know he has to login to save preferences
    private fun establishPrintSharedPreferences(): Boolean {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)

        val email = sharedPrefs.getString("email", "")
        val password = sharedPrefs.getString("password", "")

        if (email == "" || password == "") {
            showAlertFingerPrint()
            return false
        }
        return true

    }

    //it saves email and password when they are valid
    private fun saveFingerSharedPreferences() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        val editor = sharedPrefs.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()


    }

    //it loads the email and password in the fields
    private fun loadUserFingerPrintPreferences() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val email = sharedPrefs.getString("email", "")
        val password = sharedPrefs.getString("password", "")

        binding.etEmail.setText(email)
        binding.etPassword.setText(password)


    }

    //it shows an alert to the user about login to get the preferences
    private fun showAlertFingerPrint() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.alert_title)
        builder.setMessage(R.string.alert_message)
        val dialog = builder.create()
        dialog.show()
    }


}

