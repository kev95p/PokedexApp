package com.kev95p.pruebakotlin.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kev95p.pruebakotlin.R
import com.kev95p.pruebakotlin.data.dto.PokemonDto

class TeamPokemonStatListAdapter(private val dataSet:ArrayList<PokemonDto?>,private val ctx: Context, private val onClickListener: OnClickItemListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val pokemonImage: ImageView = view.findViewById(R.id.teamPokemonImage)
        val pokemonName: TextView = view.findViewById(R.id.teamPokemonName)
        val txtHp: TextView = view.findViewById(R.id.txtHp)
        val txtAtk: TextView = view.findViewById(R.id.txtAtk)
        val txtDef: TextView = view.findViewById(R.id.txtDef)
        val txtSpAtk: TextView = view.findViewById(R.id.txtSpAtk)
        val txtSpDef: TextView = view.findViewById(R.id.txtSpDef)
        val txtSpeed: TextView = view.findViewById(R.id.txtSpeed)
        val barHp: ProgressBar = view.findViewById(R.id.barHp)
        val barAtk: ProgressBar = view.findViewById(R.id.barAtk)
        val barDef: ProgressBar = view.findViewById(R.id.barDef)
        val barSpDef: ProgressBar = view.findViewById(R.id.barSpDef)
        val barSpAtk: ProgressBar = view.findViewById(R.id.barSpAtk)
        val barSpeed: ProgressBar = view.findViewById(R.id.barSpeed)
        val cardView: CardView = view.findViewById(R.id.pokemon_stat_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_pokemon_stat_item, parent, false)
        Log.d("TeamPokemonStat","create view holder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        val pkmn = dataSet[position]
        Log.d("TeamPokemonStat",pkmn.toString())
        holder.pokemonName.text = pkmn?.name
        Glide.with(ctx)
            .load(pkmn?.imageSpriteUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.pokemonImage)
        holder.txtHp.text = pkmn?.hp.toString()
        holder.txtAtk.text = pkmn?.atk.toString()
        holder.txtDef.text = pkmn?.def.toString()
        holder.txtSpAtk.text = pkmn?.spAtk.toString()
        holder.txtSpDef.text = pkmn?.spDef.toString()
        holder.txtSpeed.text = pkmn?.speed.toString()
        holder.barHp.progress = pkmn?.hp!!
        holder.barAtk.progress = pkmn.atk!!
        holder.barDef.progress = pkmn.def!!
        holder.barSpDef.progress = pkmn.spDef!!
        holder.barSpAtk.progress = pkmn.spAtk!!
        holder.barSpeed.progress = pkmn.speed!!


        holder.cardView.setOnClickListener { view ->
            Log.d("TeamStatAdapter","Click")
            onClickListener.onClickItem(dataSet[position])
        }

        holder.cardView.setOnLongClickListener { view ->
            onClickListener.onLongClickItem(dataSet[position])
            true
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }



    interface OnClickItemListener{
        fun onLongClickItem(item:PokemonDto?)
        fun onClickItem(item:PokemonDto?)
    }

}