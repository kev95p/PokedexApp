package com.kev95p.pruebakotlin.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kev95p.pruebakotlin.R
import com.kev95p.pruebakotlin.data.dto.PokemonDto

class TeamPokemonListAdapter(private val dataSet:ArrayList<PokemonDto?>, private val ctx: Context)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonName: TextView = view.findViewById(R.id.txtPokemonName)
        val pokemonImage: ImageView = view.findViewById(R.id.imgPokemon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_pokemon_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        val pkmn = dataSet[position]
        holder.pokemonName.text = pkmn?.name
        Glide.with(ctx)
            .load(pkmn?.imageSpriteUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.pokemonImage)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}