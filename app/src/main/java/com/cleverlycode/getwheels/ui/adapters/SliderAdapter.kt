package com.cleverlycode.getwheels.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cleverlycode.getwheels.databinding.SlideItemContainerBinding
import com.google.firebase.storage.StorageReference

class SliderAdapter(
    private val imageStorageRefs: List<StorageReference>
) : RecyclerView.Adapter<SliderAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(val binding: SlideItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setImageView(imageRef: StorageReference) {
            Glide.with(binding.root.context)
                .load(imageRef)
                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SlideItemContainerBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setImageView(imageStorageRefs[position])
    }

    override fun getItemCount(): Int = imageStorageRefs.size
}