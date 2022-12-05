package com.google.challengesophos.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.challengesophos.Repository.model.DocItems
import com.google.challengesophos.databinding.ItemSeeDocsBinding

class ItemsDocsViewHolder (view: View) : ViewHolder(view){

    val binding = ItemSeeDocsBinding.bind(view)

    fun render(seeDocsViewModel: DocItems?){
        binding.tvAttachedType.text = seeDocsViewModel?.TipoAdjunto
        binding.tvItemFecha.text = seeDocsViewModel?.Fecha
        binding.tvNameItem.text = seeDocsViewModel?.Nombre
        binding.tvLastNameItem.text = seeDocsViewModel?.Apellido
    }
}