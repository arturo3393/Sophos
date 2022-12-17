package com.google.challengesophos.ui



import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.challengesophos.R
import com.google.challengesophos.databinding.FragmentWelcomeBinding
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
        //it loads the preferences of the language after changing between dark and light mode in different languages
        val sharedPrefsDarkLanguage = PreferenceManager.getDefaultSharedPreferences(context)

        when(sharedPrefsDarkLanguage.getString("My_Lang","")){
            "es" -> setLocate("es")
            "en"->setLocate("en")
            "fr"->setLocate("fr")
        }

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

        // it puts the title of the darkModeMenu
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

                val sharedPrefsDarkLanguage = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = sharedPrefsDarkLanguage.edit()


                when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        setDefaultNightMode(MODE_NIGHT_NO)
                        editor.putString("My_Lang", resources.configuration.locale.language)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        editor.putString("My_Lang", resources.configuration.locale.language)
                        setDefaultNightMode(MODE_NIGHT_YES)
                    }
                }
                editor.apply()
                true

            }
            R.id.languageMenu -> {
                val getStringLanguage = getString(R.string.English_language)

                when (getStringLanguage) {
                    "Idioma Inglés" -> setLocate("en")
                    "French Language" -> setLocate("fr")
                    "Langue espagnole" -> setLocate("es")
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


    //save the preferences for the language
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


}

