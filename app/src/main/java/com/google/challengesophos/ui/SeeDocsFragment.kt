package com.google.challengesophos.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.challengesophos.R
import com.google.challengesophos.ViewModel.GetDocsViewModel
import com.google.challengesophos.databinding.FragmentSeeDocsBinding
import com.google.challengesophos.ui.adapter.ItemsDocsAdapter


class SeeDocsFragment : Fragment(R.layout.fragment_see_docs) {

    private val seeDocsModel : GetDocsViewModel by viewModels()

    private var _binding: FragmentSeeDocsBinding? = null

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
        _binding = FragmentSeeDocsBinding.inflate(inflater, container, false)

        //puts the name to the appbar
        (activity as AppCompatActivity).supportActionBar?.title ="Regresar"
        //Sets the back arrow and the icon for it
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow_light)

        val email = arguments?.getString("user_email")!!

        seeDocsModel.getDocsModelLiveData.observe(viewLifecycleOwner, Observer {

            seeDocsModel.getDocsList(email)
            initRecyclerView()


        })
        seeDocsModel.getDocsList(email)






        return binding.root


    }

     private fun initRecyclerView(){
         val manager = LinearLayoutManager(context)
         val decoration = DividerItemDecoration(context,manager.orientation)
         decoration.setDrawable(resources.getDrawable(R.drawable.rv_divider))

        binding.rvDocList.layoutManager = manager
        binding.rvDocList.adapter = ItemsDocsAdapter(seeDocsModel.getDocsModelLiveData.value)

        binding.rvDocList.addItemDecoration(decoration)
    }




}