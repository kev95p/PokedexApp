package com.kev95p.pruebakotlin.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kev95p.pruebakotlin.R
import com.kev95p.pruebakotlin.data.dto.PokemonDto
import com.kev95p.pruebakotlin.data.dto.TeamDto
import com.kev95p.pruebakotlin.interfaces.AddTeam
import com.kev95p.pruebakotlin.presenter.AddTeamPresenterImpl
import com.kev95p.pruebakotlin.utils.hideKeyboard
import com.kev95p.pruebakotlin.view.adapters.PokemonListAdapter
import com.kev95p.pruebakotlin.view.adapters.PokemonSearchAdapter
import com.kev95p.pruebakotlin.view.adapters.TeamPokemonListAdapter
import com.kev95p.pruebakotlin.view.adapters.TeamPokemonStatListAdapter

class AddTeamActivity : AppCompatActivity(), AddTeam.View {

    private lateinit var txtTeamName: TextView
    private lateinit var autoSearchPokemon: AutoCompleteTextView
    private lateinit var pokemonList: ArrayList<PokemonDto?>
    private lateinit var pokemonSearchAdapter: PokemonSearchAdapter

    private lateinit var teamList: RecyclerView
    private lateinit var teamPokemonStatListAdapter: TeamPokemonStatListAdapter
    private lateinit var teamPokemonList: ArrayList<PokemonDto?>

    private lateinit var presenter: AddTeam.Presenter
    private var teamKey:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_team)
        val toolbar: Toolbar = findViewById(R.id.toolbar_add_team)
        setSupportActionBar(toolbar)
        toolbar.title = "New team"
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);





        txtTeamName = findViewById(R.id.txtTeamName)
        autoSearchPokemon = findViewById(R.id.autoSearchPokemon)

        teamList = findViewById(R.id.addTeamPokemonListt)
        teamPokemonList = ArrayList()
        teamList.adapter = TeamPokemonStatListAdapter(teamPokemonList,this)
        teamList.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        teamList.layoutManager = llm

        presenter = AddTeamPresenterImpl(this, this)
        presenter.getLocalPokemon()

        teamPokemonStatListAdapter = TeamPokemonStatListAdapter(teamPokemonList,this)


        autoSearchPokemon.setOnItemClickListener { parent, view, position, id ->
            autoSearchPokemon.setText("")
            autoSearchPokemon.hideKeyboard()
            teamPokemonList.add(pokemonSearchAdapter.getItem(position))
            teamPokemonStatListAdapter.notifyDataSetChanged()
            Log.d("AddTeamActivityclick",teamPokemonList.toString())
            if (teamPokemonList.size >= 6){
                autoSearchPokemon.visibility = View.GONE
            }
        }

        if(intent.hasExtra("key")){
            teamKey = intent.getStringExtra("key")
            teamKey?.let { presenter.getTeam(it) }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_team_menu,menu)
        return true
    }

    fun validateForm(): Boolean{
        if(txtTeamName.text.isEmpty()){
            txtTeamName.error = "Team name is required"
            return false
        }
        if(teamPokemonList.size < 3){
            Toast.makeText(this,"Please select three or more pokemon",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_team){
            val team = TeamDto(txtTeamName.text.toString(),null,teamKey,teamPokemonList)
            Log.d("AddTeamModel","saveTeamOnRdb")
            if(validateForm()){
                presenter.saveTeam(team)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun receiveLocalPokemon(data: ArrayList<PokemonDto?>) {
        pokemonList = data
        pokemonSearchAdapter = PokemonSearchAdapter(applicationContext, R.layout.pokemon_item, data)
        autoSearchPokemon.setAdapter(pokemonSearchAdapter)
    }

    override fun receiveTeam(team: TeamDto) {
        Log.d("AddTeamActivity",team.toString())
        teamPokemonList.clear()
        txtTeamName.text = team.name
        teamPokemonList.addAll(team.pokemonList!!)
        teamPokemonStatListAdapter.notifyDataSetChanged()
        autoSearchPokemon.requestFocus()
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    override fun teamSaved() {
        finish()
    }
}