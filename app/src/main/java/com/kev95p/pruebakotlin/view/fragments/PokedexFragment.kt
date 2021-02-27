package com.kev95p.pruebakotlin.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kev95p.pruebakotlin.R
import com.kev95p.pruebakotlin.data.dto.PokemonDto
import com.kev95p.pruebakotlin.interfaces.PokemonPresenter
import com.kev95p.pruebakotlin.interfaces.PokemonView
import com.kev95p.pruebakotlin.presenter.PokemonPresenterImpl
import com.kev95p.pruebakotlin.utils.OnLoadMoreListener
import com.kev95p.pruebakotlin.utils.RecyclerViewLoadMoreScroll
import com.kev95p.pruebakotlin.view.activities.LoginActivity
import com.kev95p.pruebakotlin.view.adapters.PokemonListAdapter

class PokedexFragment : Fragment(), PokemonView {

    private lateinit var pokemonList: RecyclerView
    private lateinit var initialLoading: LinearLayout
    private lateinit var searchView: SearchView
    private lateinit var pokemonListAdapter: PokemonListAdapter
    private lateinit var pokemonData: ArrayList<PokemonDto?>
    private var presenter: PokemonPresenter? = null
    val INITAL_NUMBER_ROWS = 50
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pokedex, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = activity?.baseContext?.let { PokemonPresenterImpl(this, it) }
        pokemonList = rootView.findViewById(R.id.pokemonList)
        initialLoading = rootView.findViewById(R.id.initialLoading)
        initialLoading.visibility = View.VISIBLE
        pokemonData = ArrayList()
        presenter?.getData(INITAL_NUMBER_ROWS, 0)
        pokemonList.setHasFixedSize(true)
    }

    override fun onPause() {
        super.onPause()
        presenter?.disposeService()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter = null;
    }

    override fun showData(data: ArrayList<PokemonDto?>) {
        if (pokemonData.isEmpty()) {
            pokemonData = data
            pokemonListAdapter = PokemonListAdapter(
                pokemonData,
                (activity as? AppCompatActivity),
            )
            pokemonList.adapter = pokemonListAdapter;
            initialLoading.visibility = View.GONE
        } else {
            Log.d("PokedexFragment",searchView.query.toString())
            pokemonListAdapter.addData(data)
            pokemonListAdapter.filter.filter(searchView.query.toString())
        }
        Log.d("PokedexFragment",pokemonData.size.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pokedex_menu,menu)
        val searchMenu = menu.findItem(R.id.search_pokemon)
        searchView = searchMenu.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pokemonListAdapter.filter.filter(newText)
                return false
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.close_session_dex){
            val shared = activity?.applicationContext?.getSharedPreferences("pref",0)
            val editor = shared?.edit()
            editor?.remove("currentUser")
            editor?.apply()
            val newIntent = Intent(activity?.applicationContext, LoginActivity::class.java)
            newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

            startActivity(newIntent)
        }
        return super.onOptionsItemSelected(item)
    }

}