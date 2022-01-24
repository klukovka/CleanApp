package com.example.cleanapp.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanapp.databinding.ItemProductBinding
import com.example.cleanapp.domain.entity.ProductEntity

class HomeProductAdapter (private val products: MutableList<ProductEntity>) :
            RecyclerView.Adapter<HomeProductAdapter.ViewHolder>(){

    inner class ViewHolder(private val itemBinding : ItemProductBinding) :
            RecyclerView.ViewHolder(itemBinding.root){
                fun bind(product: ProductEntity){
                    //TODO("Not yet implemented")
                }
            }

    interface OnItemTap{
        fun onTap(product: ProductEntity)
    }

    fun setOnItemTap(item : OnItemTap){
        onTapListener = item
    }

    private var onTapListener : OnItemTap? = null

    fun updateList(mProducts : List<ProductEntity>){
        products.clear()
        products.addAll(mProducts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemProductBinding.inflate(LayoutInflater.from(parent.context),
        parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(products[position])

    override fun getItemCount(): Int = products.size
}