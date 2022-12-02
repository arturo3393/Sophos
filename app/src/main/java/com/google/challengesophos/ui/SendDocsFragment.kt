package com.google.challengesophos.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels

import com.google.challengesophos.R
import com.google.challengesophos.ViewModel.LoginViewModel
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




        return binding.root

    }


    //Implemented methods of the interface AdapterView.OnItemSelectedListener (selected item)
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //Saves the answer of the user of the docs type
        val typeDocsSelected = arrayAdapterTypeDocs.getItem(position)
        TODO("Missing the way to select the answer, the arrayadapter is in the local scope, in the global is empty")
        // val citySelected = arrayAdapterCities.getItem(position)
    }

    //Implemented methods of the interface AdapterView.OnItemSelectedListener (selected item)
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    /*
    //Saves the value of the live data to make it shorter in the adapter
    private fun getCitiesIndex(index: Int): String {
        return postDocViewModel.citiesLiveData.value?.get(index)?.toString() ?: ""
    }*/

}