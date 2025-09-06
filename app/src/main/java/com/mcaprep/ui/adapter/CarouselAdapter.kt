package com.mcaprep.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mcaprep.databinding.ItemCarouselBinding


class CarouselAdapter(private val context: Context, private val itemList: List<CarouselItem>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    // ViewHolder using View Binding
    inner class CarouselViewHolder(val binding: ItemCarouselBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        // Inflate the layout using View Binding
        val binding = ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = itemList[position]
        // Access views via binding
        Glide.with(context)
            .load(item.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true)
            .fitCenter()
            .into(holder.binding.carouselImage)
    }

    override fun getItemCount(): Int = itemList.size
}

data class CarouselItem(val imageUrl: String, val title: String)