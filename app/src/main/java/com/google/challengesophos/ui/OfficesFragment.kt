package com.google.challengesophos.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

class OfficesFragment : Fragment(), OnMapReadyCallback {


    private val getOfficesViewModel: GetOfficesViewModel by viewModels()

    private var citiesObserved : MutableList<OfficeItems> = mutableListOf()



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
        //binding initialized
        _binding = FragmentOfficesBinding.inflate(inflater, container, false)


        //puts the name to the appbar
        (activity as AppCompatActivity).supportActionBar?.title = "Regresar"
        //Sets the back arrow and the icon for it
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow_light)

        getOfficesViewModel.citiesLiveData.observe(viewLifecycleOwner, Observer {
            getOfficesViewModel.getOffices()
            //saves the cities in a mutable list
            for (cities in getOfficesViewModel.citiesLiveData.value!!){
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

          for(cities in citiesObserved){
            val marker =  MarkerOptions()
                .position(LatLng(cities.Latitud.toDouble(),cities.Longitud.toDouble()))
                .title(cities.Nombre)
                map.addMarker(marker)
        }


        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(4.7109886, -74.072092), 18f), 4000, null
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
                Toast.makeText(
                    context,
                    "Set up location permissions in settings",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}