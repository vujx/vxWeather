package com.algebra.wheathertask.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.algebra.wheathertask.databinding.ItemCitiesBinding
import com.algebra.wheathertask.model.CitiesNames

class CitiesAdapter: RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>() {

    val listOfCities = mutableListOf<CitiesNames>()
    var listener: Listener ?= null

    fun setList(list: List<CitiesNames>){
        listOfCities.clear()
        listOfCities.addAll(list)
        notifyDataSetChanged()
    }

    inner class CitiesViewHolder(private val itemCities: ItemCitiesBinding): RecyclerView.ViewHolder(itemCities.root){
        init{
            itemView.setOnClickListener {
                listener?.onItemClick(listOfCities[layoutPosition])
            }
        }
        fun bind(city: CitiesNames){
            itemCities.tvCity.text = city.city
            itemCities.tvCountry.text = ", ${city.country}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        val binding = ItemCitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitiesViewHolder(binding)
    }

    override fun getItemCount(): Int = listOfCities.size

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.bind(listOfCities[position])
    }

    interface Listener{
        fun onItemClick(city: CitiesNames)
    }
}