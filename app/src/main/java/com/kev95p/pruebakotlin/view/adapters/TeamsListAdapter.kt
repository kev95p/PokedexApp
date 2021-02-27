package com.kev95p.pruebakotlin.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kev95p.pruebakotlin.R
import com.kev95p.pruebakotlin.data.dto.PokemonDto
import com.kev95p.pruebakotlin.data.dto.TeamDto

class TeamsListAdapter(
    private val dataSet: ArrayList<TeamDto?>,
    private val ctx: Context
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onClickListener: OnClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val teamName:TextView = view.findViewById(R.id.txtName)
        val teamPokemonList: RecyclerView = view.findViewById(R.id.teamPokemonList)
        val teamCardView: CardView = view.findViewById(R.id.teamCardView)
        val itemClickable: LinearLayout = view.findViewById(R.id.itemClickable)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        val team = dataSet[position]
        holder.teamName.text = team?.name
        holder.teamPokemonList.layoutManager = GridLayoutManager(ctx,3)
        holder.teamPokemonList.setHasFixedSize(true)

        val adapter = TeamPokemonListAdapter(dataSet[position]?.pokemonList as ArrayList<PokemonDto?>,ctx)
        holder.teamPokemonList.adapter = adapter

        holder.itemClickable.setOnLongClickListener{ view ->
            dataSet[position]?.let { onClickListener?.onItemLongClick(it) }
            true
        }
        holder.itemClickable.setOnClickListener { view ->
            dataSet[position]?.let { onClickListener?.onItemClick(it) }
        }

    }


    override fun getItemCount(): Int {
        return dataSet.size
    }

    interface OnClickListener{
        fun onItemClick(team:TeamDto)
        fun onItemLongClick(team:TeamDto)
    }
}