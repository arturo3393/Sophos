package com.google.challengesophos.ui


import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Base64
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.challengesophos.R
import com.google.challengesophos.ViewModel.GetDocsByIdViewModel
import com.google.challengesophos.ViewModel.GetDocsViewModel
import com.google.challengesophos.databinding.FragmentSeeDocsBinding
import com.google.challengesophos.ui.adapter.ItemsDocsAdapter
import java.util.*


class SeeDocsFragment : Fragment(R.layout.fragment_see_docs) {

    private val getDocsModel: GetDocsViewModel by viewModels()
    private val getDocsByIdViewModel: GetDocsByIdViewModel by viewModels()

    private var _binding: FragmentSeeDocsBinding? = null

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
        _binding = FragmentSeeDocsBinding.inflate(inflater, container, false)


        //enables the menu in the fragment
        setHasOptionsMenu(true)

        //puts the name to the appbar
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.come_back)
        //Sets the back arrow and the icon for it
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //it checks if it is dark or light mode and changes the back arrow
        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow_dark)

            }
            Configuration.UI_MODE_NIGHT_NO -> {
                (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow_light)
            }
        }

        val email = arguments?.getString("user_email")!!

        getDocsModel.getDocsModelLiveData.observe(viewLifecycleOwner, Observer {
            //brings the list of documents
            getDocsModel.getDocsList(email)
            initRecyclerView()

        })
        //Activates the observer and the recycler view
        getDocsModel.getDocsList(email)

        //observing the image and converting it to image
        getDocsByIdViewModel.getDocsImgMutableLiveData.observe(viewLifecycleOwner, Observer {
            val imageBase64 = getDocsByIdViewModel.getDocsImgMutableLiveData.value?.get(0)?.Adjunto
            if(imageBase64?.contains("9j",true) == true){ //Makes sure it is an img in base64
                val imgConverted = decodePicString(imageBase64)
                binding.ivSeeDocsImage.setImageBitmap(imgConverted)
            }
            else{
                Toast.makeText(context, "NO IMAGE", Toast.LENGTH_SHORT).show()
            }

        })




        return binding.root


    }

    //puts the recycler view and its divider
    private fun initRecyclerView() {
        val manager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, manager.orientation)

        //This line puts a resource as the divider
       // decoration.setDrawable(resources.getDrawable(R.drawable.rv_divider))

        binding.rvDocList.layoutManager = manager
        binding.rvDocList.adapter = ItemsDocsAdapter(getDocsModel.getDocsModelLiveData.value)

        {
            getDocsByIdViewModel.getDocsViewModel(it.IdRegistro)
        }

        binding.rvDocList.addItemDecoration(decoration)
    }

    fun decodePicString(encodedString: String): Bitmap {
        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }

    //Creates the menu
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

    //navigate to each option in the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.sendDocsMenu -> {
                view?.findNavController()
                    ?.navigate(
                        SeeDocsFragmentDirections.actionSeeDocsFragmentToWelcomeFragment(
                            arguments?.getString("user_name"),
                            arguments?.getString("user_email")
                        )
                    )
                view?.findNavController()?.navigate(
                    WelcomeFragmentDirections.actionWelcomeFragmentToSendDocsFragment(
                        arguments?.getString(
                            "user_email"
                        ),
                        arguments?.getString(
                            "user_name"
                        )
                    )
                )

                true
            }
            R.id.seeDocsMenu -> {
                //string for toast
                val alreadyInDocs = getString(R.string.already_seeing)
                Toast.makeText(context, alreadyInDocs, Toast.LENGTH_SHORT).show()
                true
            }
            R.id.officesMenu -> {
                view?.findNavController()
                    ?.navigate(
                        SeeDocsFragmentDirections.actionSeeDocsFragmentToWelcomeFragment(
                            arguments?.getString("user_name"),
                            arguments?.getString("user_email")
                        )
                    )
                view?.findNavController()?.navigate(
                    WelcomeFragmentDirections.actionWelcomeFragmentToOfficesFragment(
                        arguments?.getString(
                            "user_email"
                        ),   arguments?.getString(
                            "user_name"
                        )
                    )
                )

                true
            }
            R.id.darkModeMenu -> {
                val sharedPrefsDarkLanguage = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = sharedPrefsDarkLanguage.edit()


                when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        editor.putString("My_Lang", resources.configuration.locale.language)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        editor.putString("My_Lang", resources.configuration.locale.language)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
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
                view?.findNavController()?.navigate(R.id.action_seeDocsFragment_to_loginFragment)
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
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



    //Upload the fragment to see the language changed
    private fun navigateFragmentItself() {

        view?.findNavController()
            ?.navigate(
                SeeDocsFragmentDirections.actionSeeDocsFragmentToWelcomeFragment(
                    arguments?.getString("user_name"),
                    arguments?.getString("user_email")
                )
            )

        view?.findNavController()
            ?.navigate(
                WelcomeFragmentDirections.actionWelcomeFragmentToSeeDocsFragment(
                    arguments?.getString("user_email"),
                    arguments?.getString("user_name")
                )
            )
    }


}