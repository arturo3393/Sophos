package com.google.challengesophos.ui


import android.app.Activity
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Base64
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
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
        //binding initialized
        _binding = FragmentSeeDocsBinding.inflate(inflater, container, false)


        //enables the menu in the fragment
        setHasOptionsMenu(true)

        //puts the name to the appbar
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.come_back)
        //Sets the back arrow and the icon for it
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow_light)

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
            val imgConverted = decodePicString(imageBase64!!)
            binding.ivSeeDocsImage.setImageBitmap(imgConverted)
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
    }

    //navigate to each option in the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sendDocsMenu -> {
                view?.findNavController()?.navigate(
                    SeeDocsFragmentDirections.actionSeeDocsFragmentToSendDocsFragment(
                        arguments?.getString(
                            "user_email"
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
                view?.findNavController()?.navigate(
                    SeeDocsFragmentDirections.actionSeeDocsFragmentToOfficesFragment(
                        arguments?.getString(
                            "user_email"
                        )
                    )
                )
                true
            }
            R.id.darkModeMenu -> {

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
                SeeDocsFragmentDirections.actionSeeDocsFragmentSelf(
                    arguments?.getString("user_email"),
                )
            )
    }


}