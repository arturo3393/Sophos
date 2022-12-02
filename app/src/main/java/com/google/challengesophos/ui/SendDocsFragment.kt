package com.google.challengesophos.ui

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.challengesophos.R
import com.google.challengesophos.ViewModel.PostDocViewModel
import com.google.challengesophos.databinding.FragmentSendDocsBinding
import java.util.*

class SendDocsFragment : Fragment(R.layout.fragment_send_docs), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentSendDocsBinding? = null

    private val postDocViewModel: PostDocViewModel by viewModels()

    //Adapter for the spinner of docs type
    lateinit var arrayAdapterTypeDocs: ArrayAdapter<String>

    //Adapter fot the spiner of cities
    lateinit var arrayAdapterCities: ArrayAdapter<String>

    //The code of the permission for the camera
    private val PERMISSION_CAMARA:Int = 100

    //The code of the permission for the storage
    private val PERMISSION_EXTERNAL_STORAGE: Int = 100


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

        //puts the name to the appbar
        (activity as AppCompatActivity).supportActionBar?.title = "Regresar"
        //Sets the back arrow and the icon for it
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow_light)


        //Adapter for the spinner of docs type
        arrayAdapterTypeDocs =
            ArrayAdapter<String>(requireContext(), R.layout.spinner_styles_light)

        //Adapter for the spinner of cities
        arrayAdapterCities = ArrayAdapter<String>(requireContext(), R.layout.spinner_styles_light)

        //puts info inside the spinner
        arrayAdapterTypeDocs.addAll(Arrays.asList("Tipo de documento", "CC", "TI", "PA", "CE"))

        //Observes the cities that the Api brings


        postDocViewModel.citiesLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            arrayAdapterCities.addAll(
                Arrays.asList(
                    "Ciudad",
                    postDocViewModel.citiesLiveData.value?.get(0)?.toString() ?: "",
                    postDocViewModel.citiesLiveData.value?.get(1)?.toString() ?: "",
                    postDocViewModel.citiesLiveData.value?.get(2)?.toString() ?: "",
                    postDocViewModel.citiesLiveData.value?.get(3)?.toString() ?: "",
                    postDocViewModel.citiesLiveData.value?.get(4)?.toString() ?: "",
                    postDocViewModel.citiesLiveData.value?.get(5)?.toString() ?: ""

                )
            )

        })


        //Establishes the array as the adapter of the spinner
        binding.spDocType.adapter = arrayAdapterTypeDocs
        binding.spDocCity.adapter = arrayAdapterCities

        //Configures the content of the selection
        binding.spDocType.onItemSelectedListener = this
        binding.spDocCity.onItemSelectedListener = this

        binding.etEmailSendDocs.setText(arguments?.getString("user_email"))

        binding.ivTakePhotoDocs.setOnClickListener {
            askForCameraPermission()
        }

        binding.btnAttachDoc.setOnClickListener {
            askForFilesPermission()

            Toast.makeText(
                context,
                "TWEEEE.",
                Toast.LENGTH_SHORT
            ).show()

        }


        return binding.root



    }


    //Implemented methods of the interface AdapterView.OnItemSelectedListener (selected item)
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //Saves the answer of the user of the docs type
        val typeDocsSelected = arrayAdapterTypeDocs.getItem(position)
        //"Missing the way to select the answer, the arrayadapter is in the local scope, in the global is empty"
        // val citySelected = arrayAdapterCities.getItem(position)
    }

    //Implemented methods of the interface AdapterView.OnItemSelectedListener (selected item)
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    //1st the app already has permission, 2. Permission was denied previously. 3. First time the app ask for permission
    private fun askForCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                takePhoto()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                Toast.makeText(
                    context,
                    "The permission was previously denied, allow it in settings.",
                    Toast.LENGTH_SHORT
                ).show()

            }
            else -> {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), PERMISSION_CAMARA)
            }

        }
    }

    private fun askForFilesPermission(){
        when {
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> {
                uploadPhoto()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                Toast.makeText(
                    context,
                    "The permission was previously denied, allow it in settings.",
                    Toast.LENGTH_SHORT
                ).show()

            }
            else -> {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_EXTERNAL_STORAGE)
            }

        }


    }

    //allows to evaluate the result of the permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CAMARA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        takePhoto()
                }
            }

            PERMISSION_EXTERNAL_STORAGE-> {
                if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    uploadPhoto()
            }
            else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }

    }

    private fun takePhoto() {

    }

    private fun uploadPhoto(){

    }


}


/*
//Saves the value of the live data to make it shorter in the adapter
private fun getCitiesIndex(index: Int): String {
    return postDocViewModel.citiesLiveData.value?.get(index)?.toString() ?: ""
}*/

