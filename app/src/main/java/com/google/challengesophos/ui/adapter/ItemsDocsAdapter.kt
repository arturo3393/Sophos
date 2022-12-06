package com.google.challengesophos.ui.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.challengesophos.R
import com.google.challengesophos.Repository.model.DocItems
import com.google.challengesophos.databinding.ItemSeeDocsBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class ItemsDocsAdapter(private val getDocsViewModel: List<DocItems>?, private val onItemSelected: (DocItems)-> Unit): RecyclerView.Adapter<ItemsDocsViewHolder>()  {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsDocsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemsDocsViewHolder(layoutInflater.inflate(R.layout.item_see_docs, parent, false))
    }

    override fun onBindViewHolder(holder: ItemsDocsViewHolder, position: Int) {
        val item = getDocsViewModel?.get(position)
        holder.render(item, onItemSelected)

    }

    override fun getItemCount(): Int {
       return getDocsViewModel?.size ?: 3
    }


}

class ItemsDocsViewHolder (view: View) : RecyclerView.ViewHolder(view){


    val binding = ItemSeeDocsBinding.bind(view)

    val image = view.findViewById<ImageView>(R.id.ivSeeDocsImage)

    fun render(getDocsViewModel: DocItems?, onItemSelected: (DocItems)-> Unit  ){
        binding.tvAttachedType.text = getDocsViewModel?.TipoAdjunto
        binding.tvItemFecha.text = getDocsViewModel?.Fecha
        binding.tvNameItem.text = getDocsViewModel?.Nombre
        binding.tvLastNameItem.text = getDocsViewModel?.Apellido

        binding.root.setOnClickListener{
            if (getDocsViewModel != null) {
                onItemSelected(getDocsViewModel)
            } else{
                println("RE PAILA")
            }
        }

//       image.setImageBitmap(decodePicString(seeDocsViewModel.Adjunto))


        //Instead of this pass a lambda in the Adapter


    }

    fun decodePicString (encodedString: String): Bitmap {

        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }


}

