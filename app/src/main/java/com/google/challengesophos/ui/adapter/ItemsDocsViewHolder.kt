package com.google.challengesophos.ui.adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.challengesophos.Repository.model.DocItems
import com.google.challengesophos.databinding.ItemSeeDocsBinding

class ItemsDocsViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    val binding = ItemSeeDocsBinding.bind(view)

    fun render(getDocsViewModel: DocItems?, onItemSelected: (DocItems) -> Unit) {
        binding.tvAttachedType.text = getDocsViewModel?.TipoAdjunto
        binding.tvItemFecha.text = getDocsViewModel?.Fecha
        binding.tvNameItem.text = getDocsViewModel?.Nombre
        binding.tvLastNameItem.text = getDocsViewModel?.Apellido

        binding.root.setOnClickListener {
            if (getDocsViewModel != null) {
                onItemSelected(getDocsViewModel)

            }
        }
    }
}