package com.kev95p.pruebakotlin.model

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kev95p.pruebakotlin.data.dblocal.AppDatabase
import com.kev95p.pruebakotlin.data.dto.PokemonDto
import com.kev95p.pruebakotlin.data.dto.TeamDto
import com.kev95p.pruebakotlin.interfaces.AddTeam
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AddTeamModelImpl(private val presenter:AddTeam.Presenter,private val ctx: Context) : AddTeam.Model{

    var disposable:Disposable? = null
    private val database = FirebaseDatabase.getInstance()
    //val teamsRef = database.getReference("teams")
    val pkmnRef = database.getReference("pkmn")
    val usersRef = database.getReference("users")

    private val pokemonDao = AppDatabase.getDatabase(ctx)
        .pokemonDao()

    override fun getLocalPokemon() {
        disposable = Observable.create<List<PokemonDto?>>{ emmiter ->
            val pokemon = pokemonDao.getAll()
            emmiter.onNext(pokemon)
            emmiter.onComplete()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                presenter.receiveLocalPokemon(data as ArrayList<PokemonDto?>)
            }
    }

    override fun getTeamFromRdb(key: String) {
        val shared = ctx.applicationContext.getSharedPreferences("pref", 0)
        val userKey = shared.getString("currentUser","")
        val userTeamsRef = usersRef.child(userKey!!).child("teams").child(key)

        userTeamsRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(teamSnapsh: DataSnapshot) {

                val  pkmnKeys = teamSnapsh.child("pokemon").value as List<*>

                pkmnRef.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val pkmns = ArrayList<PokemonDto>()
                        pkmnKeys.forEach { pkmnKey ->
                            val result = snapshot.children.find { (pkmnKey as String) == it.key }?.value as HashMap<*,*>
                            val pkmnDto = PokemonDto(
                                (result["id"] as Long).toInt(),
                                result["name"] as String?,
                                result["imageUrl"] as String?,
                                result["region"] as String?,
                                result["type1"] as String?,
                                result["type2"] as String?,
                                result["imageSpriteUrl"] as String?,
                                (result["hp"] as Long).toInt(),
                                (result["atk"] as Long).toInt(),
                                (result["def"] as Long).toInt(),
                                (result["spAtk"] as Long).toInt(),
                                (result["spDef"] as Long).toInt(),
                                (result["speed"] as Long).toInt(),
                            )
                            pkmns.add(pkmnDto)
                        }
                        val team = TeamDto(teamSnapsh.child("name").value as String?,teamSnapsh.child("pokemon").value as List<String>?,key,pkmns)
                        presenter.receiveTeam(team)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun saveTeamOnRdb(team: TeamDto) {
        Log.d("AddTeamModel","saveTeamOnRdb")
        val shared = ctx.applicationContext.getSharedPreferences("pref", 0)
        val userKey = shared.getString("currentUser","")
        val userTeamsRef = usersRef.child(userKey!!).child("teams")
        val messagesListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pkmnKeys = ArrayList<String>()
                team.pokemonList?.forEach { pkmn ->
                    val result =  dataSnapshot.children.filter { (it.child("id").value as Long).toInt() == pkmn?.id }
                    if(result.isNotEmpty()){
                        result[0].key?.let { pkmnKeys.add(it) }
                    }
                    else{
                        val key = pkmnRef.push().key.toString()
                        pkmnRef.child(key).setValue(pkmn)
                        pkmnKeys.add(key)
                    }
                }
                team.pokemon = pkmnKeys
                var key = team.key
                if (key != null) {
                    Log.d("AddTeamModel",key)
                }
                if(key == null )
                    key = userTeamsRef.push().key.toString()
                Log.d("AddTeamModel",key)
                team.pokemonList = null
                userTeamsRef.child(key).setValue(team)
                presenter.teamSaved()
                Log.d("AddTeamModel",pkmnKeys.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }
        pkmnRef.addListenerForSingleValueEvent(messagesListener)
    }
}