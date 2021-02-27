package com.kev95p.pruebakotlin.view.adapters

import android.graphics.Color
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kev95p.pruebakotlin.R
import com.kev95p.pruebakotlin.data.dto.PokemonDto
import com.kev95p.pruebakotlin.utils.Constant

class PokemonListAdapter(
    private var dataSet: ArrayList<PokemonDto?>,
    private val activity: AppCompatActivity?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ActionMode.Callback, Filterable {

    private var multiselect = false
    private val selectedPokemon = ArrayList<PokemonDto?>()
    private var fullPokemon = ArrayList(dataSet)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonName: TextView = view.findViewById(R.id.pokemonName)
        val pokemonImage: ImageView = view.findViewById(R.id.pokemonImage)
        val pokemonType1: ConstraintLayout = view.findViewById(R.id.typeContainer1)
        val pokemonType2: ConstraintLayout = view.findViewById(R.id.typeContainer2)
        val pokemonTypeText1: TextView = view.findViewById(R.id.typeText1)
        val pokemonTypeText2: TextView = view.findViewById(R.id.typeText2)
        val selectionOverlay: LinearLayout = view.findViewById(R.id.overlay)
    }

    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun addData(data: ArrayList<PokemonDto?>) {
        //dataSet.addAll(data)
        fullPokemon.addAll(data)
        notifyItemRangeInserted(fullPokemon.size - data.size, fullPokemon.size)
        //notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.pokemon_item, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.progress_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {
            holder as ViewHolder

            if (selectedPokemon.contains(dataSet[position])) {
                holder.selectionOverlay.alpha = 0.3f
                holder.selectionOverlay.visibility = View.VISIBLE
            } else {
                holder.selectionOverlay.visibility = View.GONE
            }

            val name = dataSet[position]?.name!!
            if (dataSet[position]?.type2 != null) {
                holder.pokemonType1.visibility = View.VISIBLE
                holder.pokemonTypeText1.text = dataSet[position]?.type1
                holder.pokemonType1.setBackgroundColor(Color.parseColor(Constant.TYPES_COLOR[dataSet[position]?.type1]))
                holder.pokemonTypeText2.text = dataSet[position]?.type2
                holder.pokemonType2.setBackgroundColor(Color.parseColor(Constant.TYPES_COLOR[dataSet[position]?.type2]))


            } else {
                holder.pokemonType1.visibility = View.GONE
                holder.pokemonTypeText2.text = dataSet[position]?.type1
                holder.pokemonType2.setBackgroundColor(Color.parseColor(Constant.TYPES_COLOR[dataSet[position]?.type1]))

            }
            holder.pokemonName.text = name[0].toUpperCase() + name.substring(1)
            activity?.applicationContext?.let {
                Glide.with(it)
                    .load(dataSet[position]?.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.pokemonImage)
            }

            holder.itemView.setOnClickListener {
                if (multiselect)
                    selectItem(holder, dataSet[position])
                else
                    Log.d("PokemonListAdapater", "Show detail")
            }
            val action = this;
            holder.itemView.setOnLongClickListener {
                if (!multiselect) {
                    multiselect = true
                    activity?.startSupportActionMode(action)
                    selectItem(holder, dataSet[position])
                }
                true
            }
        }

    }

    override fun getItemCount() = dataSet.size

    override fun getItemViewType(position: Int): Int {
        return if (dataSet[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }

    private fun selectItem(holder: ViewHolder, pokemon: PokemonDto?) {
        if (selectedPokemon.contains(pokemon)) {
            selectedPokemon.remove(pokemon)
            holder.selectionOverlay.visibility = View.GONE
        } else {
            selectedPokemon.add(pokemon)
            holder.selectionOverlay.alpha = 0.3f
            holder.selectionOverlay.visibility = View.VISIBLE
        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        val inflater = mode?.menuInflater
        inflater?.inflate(R.menu.pokedex_toolbar_selection_menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_delete) {
            // Delete button is clicked, handle the deletion and finish the multi select process
            Toast.makeText(
                activity?.applicationContext,
                "Selected images deleted",
                Toast.LENGTH_SHORT
            ).show()
            selectedPokemon.forEach { pkmn ->
                println(pkmn.toString())
            }
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        multiselect = false
        selectedPokemon.clear()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<PokemonDto?>()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(fullPokemon)
            } else {
                val filterText = constraint.toString().toLowerCase().trim()
                filteredList.addAll(fullPokemon.filter {
                    it?.name?.contains(filterText)!!
                })
            }
            val filterResult = FilterResults()
            filterResult.values = filteredList
            return filterResult
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            dataSet.clear()
            dataSet.addAll(results?.values as List<PokemonDto?>)
            notifyDataSetChanged()

        }

    }

}