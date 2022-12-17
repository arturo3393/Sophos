package com.google.challengesophos.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.challengesophos.R
import com.google.challengesophos.Repository.model.OfficeItems
import com.google.challengesophos.ViewModel.GetOfficesViewModel
import com.google.challengesophos.databinding.FragmentOfficesBinding
import java.util.*

class OfficesFragment : Fragment(), OnMapReadyCallback {


    private val getOfficesViewModel: GetOfficesViewModel by viewModels()

    private var citiesObserved: MutableList<OfficeItems> = mutableListOf()


    private val REQUEST_CODE_LOCATION = 0

    private lateinit var map: GoogleMap

    private var _binding: FragmentOfficesBinding? = null

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
        _binding = FragmentOfficesBinding.inflate(inflater, container, false)


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

        getOfficesViewModel.citiesLiveData.observe(viewLifecycleOwner, Observer {
            getOfficesViewModel.getOffices()
            //saves the cities in a mutable list
            for (cities in getOfficesViewModel.citiesLiveData.value!!) {
                citiesObserved.add(cities)
            }

            createMarker()

        })

        createFragment()


        return binding.root

    }

    private fun createFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
        enableLocation()

    }

    private fun createMarker() {

        for (cities in citiesObserved) {
            val marker = MarkerOptions()
                .position(LatLng(cities.Latitud.toDouble(), cities.Longitud.toDouble()))
                .title(cities.Nombre)
            map.addMarker(marker)
        }


        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(4.7109886, -74.072092), 12f), 4000, null
        )
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }

    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(context, "Set up location permissions in settings", Toast.LENGTH_SHORT)
                .show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true
            } else {
                //String for toast
                val locationPermission = getString(R.string.location_permission)
                Toast.makeText(
                    context,
                    locationPermission,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
                navigateToSendDocs()
                true
            }
            R.id.seeDocsMenu -> {
                navigateToSeeDocs()
                true
            }
            R.id.officesMenu -> {
                //string for message
                val alreadyInOffices = getString(R.string.already_offices)
                Toast.makeText(context, alreadyInOffices, Toast.LENGTH_SHORT)
                    .show()
                true
            }
            R.id.darkModeMenu -> {
                //it allows the map to be initialized before the theme changed
                navigateFragmentItself()

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
                view?.findNavController()?.navigate(R.id.action_officesFragment_to_loginFragment)
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun navigateToSendDocs() {
        view?.findNavController()
            ?.navigate(
                OfficesFragmentDirections.actionOfficesFragmentToWelcomeFragment(
                    arguments?.getString("user_name"),
                    arguments?.getString("user_email")
                )
            )
        view?.findNavController()?.navigate(
            WelcomeFragmentDirections.actionWelcomeFragmentToSendDocsFragment(
                arguments?.getString("user_email"),
                arguments?.getString("user_name")
            )
        )
    }

    private fun navigateToSeeDocs() {
        view?.findNavController()
            ?.navigate(
                OfficesFragmentDirections.actionOfficesFragmentToWelcomeFragment(
                    arguments?.getString("user_name"),
                    arguments?.getString("user_email")
                )
            )

        view?.findNavController()?.navigate(
            WelcomeFragmentDirections.actionWelcomeFragmentToSeeDocsFragment(
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


    //Upload the fragment to see the language changed
    private fun navigateFragmentItself() {
        view?.findNavController()
            ?.navigate(
                OfficesFragmentDirections.actionOfficesFragmentToWelcomeFragment(
                    arguments?.getString("user_name"),
                    arguments?.getString("user_email")
                )
            )

        view?.findNavController()
            ?.navigate(
                WelcomeFragmentDirections.actionWelcomeFragmentToOfficesFragment(
                    arguments?.getString("user_email"),
                    arguments?.getString("user_name")
                )
            )
    }


}