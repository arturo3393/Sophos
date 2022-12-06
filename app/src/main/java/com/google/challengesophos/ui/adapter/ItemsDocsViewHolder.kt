package com.google.challengesophos.ui.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.challengesophos.R
import com.google.challengesophos.Repository.model.DocItems
import com.google.challengesophos.Repository.model.DocResponse
import com.google.challengesophos.ViewModel.GetDocsByIdViewModel
import com.google.challengesophos.databinding.ItemSeeDocsBinding
/*
class ItemsDocsViewHolder (view: View ) : ViewHolder(view){


    val binding = ItemSeeDocsBinding.bind(view)

    val image = view.findViewById<ImageView>(R.id.ivSeeDocsImage)

    fun render(getDocsViewModel: DocItems?){
        binding.tvAttachedType.text = getDocsViewModel?.TipoAdjunto
        binding.tvItemFecha.text = getDocsViewModel?.Fecha
        binding.tvNameItem.text = getDocsViewModel?.Nombre
        binding.tvLastNameItem.text = getDocsViewModel?.Apellido

//       image.setImageBitmap(decodePicString(seeDocsViewModel.Adjunto))


        //Insted of this pass a lambda in the Adapter

       itemView.setOnClickListener {
           Toast.makeText(binding.tvItemFecha.context,"I'm hereeeeeeeeee", Toast.LENGTH_LONG).show()
        //   image.setImageBitmap(decodePicString(seeDocsViewModel))
           println(getDocsViewModel?.TipoAdjunto)
           println(getDocsViewModel?.Adjunto)
       }
    }

    fun decodePicString (encodedString: String): Bitmap {

        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }


}*/