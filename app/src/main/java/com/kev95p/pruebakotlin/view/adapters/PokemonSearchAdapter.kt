package com.kev95p.pruebakotlin.view.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kev95p.pruebakotlin.R
import com.kev95p.pruebakotlin.data.dto.PokemonDto
import com.kev95p.pruebakotlin.utils.Constant

class PokemonSearchAdapter(context: Context, resource: Int, dataset: ArrayList<PokemonDto?>) :
    ArrayAdapter<PokemonDto>(context, resource, dataset) {

    private var items: ArrayList<PokemonDto?>
    private var itemsAll: ArrayList<PokemonDto?>
    private var suggestions: ArrayList<PokemonDto?>
    private var resourceId: Int
    private var listener: OnItemSelectedListener? = null

    init {
        items = dataset
        itemsAll = ArrayList()
        itemsAll.addAll(items)
        suggestions = ArrayList()
        resourceId = resource
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater =
                LayoutInflater.from(parent.context)
            view = inflater.inflate(resourceId, null)
        }
        val pokemon = items[position]
        if (pokemon != null) {
            val imgView: ImageView? = view?.findViewById(R.id.pokemonImage)
            val pokemonName: TextView? = view?.findViewById(R.id.pokemonName)
            val pokemonType1: ConstraintLayout? = view?.findViewById(R.id.typeContainer1)
            val pokemonType2: ConstraintLayout? = view?.findViewById(R.id.typeContainer2)
            val pokemonTypeText1: TextView? = view?.findViewById(R.id.typeText1)
            val pokemonTypeText2: TextView? = view?.findViewById(R.id.typeText2)
            //val selectionOverlay: LinearLayout? = view?.findViewById(R.id.overlay)

            val name = items[position]?.name!!
            if (items[position]?.type2 != null) {
                pokemonType1?.visibility = View.VISIBLE
                pokemonTypeText1?.text = items[position]?.type1
                pokemonType1?.setBackgroundColor(Color.parseColor(Constant.TYPES_COLOR[items[position]?.type1]))
                pokemonTypeText2?.text = items[position]?.type2
                pokemonType2?.setBackgroundColor(Color.parseColor(Constant.TYPES_COLOR[items[position]?.type2]))


            } else {
                pokemonType1?.visibility = View.GONE
                pokemonTypeText2?.text = items[position]?.type1
                pokemonType2?.setBackgroundColor(Color.parseColor(Constant.TYPES_COLOR[items[position]?.type1]))
            }
            pokemonName?.text = name[0].toUpperCase() + name.substring(1)
            Glide.with(context)
                .load(items[position]?.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgView!!)

        }
        return view!!
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            if (constraint != null) {
                suggestions.clear()
                val filtered = itemsAll.filter {
                    it?.name?.startsWith(constraint.toString().toLowerCase().trim())!!
                }
                suggestions.addAll(filtered)
                val filterResult = FilterResults()
                filterResult.values = suggestions
                filterResult.count = suggestions.size
                return filterResult
            } else {
                return FilterResults()
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if(results != null && results.count > 0){
                val filtered = results?.values as ArrayList<PokemonDto?>
                clear()
                if (filtered != null) {
                    addAll(filtered)
                }
                notifyDataSetChanged()
            }
        }
    }

    fun setOnItemSelectedListener(l:OnItemSelectedListener){
        listener = l
    }

    interface OnItemSelectedListener{
        fun onItemSelected(pokemon:PokemonDto?)
    }
}