package com.google.challengesophos.ui


import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.MenuProvider
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navArgument
import com.google.challengesophos.R
import com.google.challengesophos.databinding.FragmentWelcomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class WelcomeFragment : Fragment() {


    private var _binding: FragmentWelcomeBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //binding initialized
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        //enables the menu in the fragment
        setHasOptionsMenu(true)

        //puts the name to the appbar
        (activity as AppCompatActivity).supportActionBar?.title = arguments?.getString("user_name")
        //disables the back array
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)


        binding.btnSendDocs.setOnClickListener {

            view?.findNavController()
                ?.navigate(
                    WelcomeFragmentDirections.actionWelcomeFragmentToSendDocsFragment(
                        arguments?.getString("user_email"),
                        arguments?.getString("user_name")
                    )
                )
        }

        binding.btnSeeDocs.setOnClickListener {
            view?.findNavController()?.navigate(
                WelcomeFragmentDirections.actionWelcomeFragmentToSeeDocsFragment(
                    arguments?.getString("user_email"),
                    arguments?.getString("user_name")
                )
            )

        }

        binding.btnOffices.setOnClickListener {
            view?.findNavController()?.navigate(
                WelcomeFragmentDirections.actionWelcomeFragmentToOfficesFragment(
                    arguments?.getString("user_email"),
                    arguments?.getString("user_name")
                )
            )
        }


        return binding.root
    }


    //creates the menu in welcome fragment
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)


        val darkModeString = getString(R.string.dark_mode)
        val lightModeString = getString(R.string.light_mode)

        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                menu.findItem(R.id.darkModeMenu).title = lightModeString

            }
            Configuration.UI_MODE_NIGHT_NO -> {
                menu.findItem(R.id.darkModeMenu).title = darkModeString

            }
        }


      when(menu.findItem(R.id.darkModeMenu).title.toString()) {
            "Modo oscuro" -> loadLocateSpanish()
            "Dark Mode" -> loadLocateEnglish()
            "Thème sombre" -> loadLocateFrench()
            "Modo claro" -> loadLocateSpanish()
            "Light Mode" -> loadLocateEnglish()
            "Thème clair" -> loadLocateFrench()
        }
    }


    //navigate to each option in the menu from the welcome fragment
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sendDocsMenu -> {
                navigateToSendDocs()
                true
            }
            R.id.seeDocsMenu -> {
                navigateToSeeDocs()
                true
            }
            R.id.officesMenu -> {
                navigateToOffices()
                true
            }
            R.id.darkModeMenu -> {

                when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        setDefaultNightMode(MODE_NIGHT_NO)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        setDefaultNightMode(MODE_NIGHT_YES)
                    }
                }







                //   saveModeSharedPreferences()

/*
             val darkModeString = getString(R.string.dark_mode)
             val lightModeString = getString(R.string.light_mode)

             val sharedPrefs =PreferenceManager.getDefaultSharedPreferences(context)
             val editor = sharedPrefs.edit()


             editor.putString("DarkMode", darkModeString)
             editor.putString("LightMode", lightModeString)
             editor.apply()

             val darkMode = sharedPrefs.getString("DarkMode","")
             val lightMode = sharedPrefs.getString("LightMode","") */

                /*


                val appSettingPrefs =
                    (activity as AppCompatActivity).getPreferences(MODE_PRIVATE)
                val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
                val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)

                when (isNightModeOn) {
                    true -> {
                        setDefaultNightMode(MODE_NIGHT_NO)
                        sharedPrefsEdit.putBoolean("NightMode", false)
                        sharedPrefsEdit.apply()
                    }
                    else -> {
                        setDefaultNightMode(MODE_NIGHT_YES)
                        sharedPrefsEdit.putBoolean("NightMode", true)
                        sharedPrefsEdit.apply()
                    }
                }*/
                /*
                when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        item.setTitle(R.string.light_mode)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        item.setTitle(R.string.dark_mode)
                    }

                }*/


                /*    val appSettingPrefs =
                         (activity as AppCompatActivity).getPreferences(0)
                     val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
                     val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)

                     if (isNightModeOn) {
                         setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                         sharedPrefsEdit.putBoolean("NightMode", false)
                         sharedPrefsEdit.apply()

                     } else {
                         setDefaultNightMode(MODE_NIGHT_YES)
                         sharedPrefsEdit.putBoolean("NightMode", true)
                         sharedPrefsEdit.apply()

                     } */

                true

            }


            R.id.languageMenu -> {
                val getStringLanguage = getString(R.string.English_language)

                when (getStringLanguage) {
                    "Idioma Inglés" -> loadLocateEnglish()
                    "French Language" -> loadLocateFrench()
                    "Langue espagnole" -> loadLocateSpanish()
                }

                navigateFragmentItself()

                true
            }

            R.id.logoutMenu -> {
                view?.findNavController()?.navigate(R.id.action_welcomeFragment_to_loginFragment)
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun navigateToSendDocs() {
        view?.findNavController()?.navigate(
            WelcomeFragmentDirections.actionWelcomeFragmentToSendDocsFragment(
                arguments?.getString("user_email"),
                arguments?.getString("user_name")
            )
        )
    }

    private fun navigateToSeeDocs() {
        view?.findNavController()?.navigate(
            WelcomeFragmentDirections.actionWelcomeFragmentToSeeDocsFragment(
                arguments?.getString("user_email"),
                arguments?.getString(
                    "user_name"
                )
            )
        )
    }

    private fun navigateToOffices() {
        view?.findNavController()?.navigate(
            WelcomeFragmentDirections.actionWelcomeFragmentToOfficesFragment(
                arguments?.getString("user_email"),
                arguments?.getString("user_name")
            )
        )
    }


    //save the preferences
    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, requireContext().resources.displayMetrics)
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPrefs.edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    //Write the preferences
    private fun loadLocateSpanish() {

        val sharedPreferences =
            (activity as AppCompatActivity).getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        if (language != null) {
            setLocate(language)
        }

    }

    private fun loadLocateEnglish() {

        val sharedPreferences =
            (activity as AppCompatActivity).getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "en")
        if (language != null) {
            setLocate(language)
        }
    }

    private fun loadLocateFrench() {

        val sharedPreferences =
            (activity as AppCompatActivity).getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "fr")
        if (language != null) {
            setLocate(language)
        }
    }

    //Upload the fragment to see the language changed
    private fun navigateFragmentItself() {
        view?.findNavController()
            ?.navigate(
                WelcomeFragmentDirections.actionWelcomeFragmentSelf(
                    arguments?.getString("user_name"),
                    arguments?.getString("user_email")
                )
            )
    }


    //it saves email and password when they are valid
    private fun saveModeSharedPreferences() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPrefs.edit()

        val darkModeString = getString(R.string.dark_mode)
        val lightModeString = getString(R.string.light_mode)

        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                editor.putString("LightMode", lightModeString)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                editor.putString("DarkMode", darkModeString)
            }
        }
        editor.apply()
    }
}


/*






 */