package com.google.challengesophos.ui


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.challengesophos.R
import com.google.challengesophos.ViewModel.GetDocsByIdViewModel
import com.google.challengesophos.ViewModel.GetDocsViewModel
import com.google.challengesophos.databinding.FragmentSeeDocsBinding
import com.google.challengesophos.ui.adapter.ItemsDocsAdapter


class SeeDocsFragment : Fragment(R.layout.fragment_see_docs) {

    private val getDocsModel : GetDocsViewModel by viewModels()
    private val getDocsByIdViewModel: GetDocsByIdViewModel by viewModels()

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

     private fun initRecyclerView(){
         val manager = LinearLayoutManager(context)
         val decoration = DividerItemDecoration(context,manager.orientation)
         decoration.setDrawable(resources.getDrawable(R.drawable.rv_divider))

        binding.rvDocList.layoutManager = manager
        binding.rvDocList.adapter = ItemsDocsAdapter(getDocsModel.getDocsModelLiveData.value)

        {
            getDocsByIdViewModel.getDocsViewModel(it.IdRegistro)
        }

        binding.rvDocList.addItemDecoration(decoration)
    }

    fun decodePicString (encodedString: String): Bitmap {
        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }



}