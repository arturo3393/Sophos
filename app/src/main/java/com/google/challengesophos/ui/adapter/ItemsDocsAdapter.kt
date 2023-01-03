package com.google.challengesophos.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.challengesophos.R
import com.google.challengesophos.repository.model.DocItems

class ItemsDocsAdapter(
    private val getDocsViewModel: List<DocItems>?,
    private val onItemSelected: (DocItems) -> Unit
) : RecyclerView.Adapter<ItemsDocsViewHolder>() {


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


