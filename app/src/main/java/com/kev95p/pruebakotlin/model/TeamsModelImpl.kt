package com.kev95p.pruebakotlin.model

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kev95p.pruebakotlin.data.dto.PokemonDto
import com.kev95p.pruebakotlin.data.dto.TeamDto
import com.kev95p.pruebakotlin.interfaces.Teams

class TeamsModelImpl(private val presenter: Teams.Presenter,private val ctx: Context): Teams.Model {
    private val database = FirebaseDatabase.getInstance()
    val pkmnRef = database.getReference("pkmn")
    val usersRef = database.getReference("users")

    override fun fetchTeamsRdb() {
        val shared = ctx.applicationContext.getSharedPreferences("pref", 0)
        val userKey = shared.getString("currentUser","")
        val userTeamsRef = usersRef.child(userKey!!).child("teams")
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val teams = ArrayList<TeamDto?>()
                snapshot.children.forEach { child->
                    val team: TeamDto? = TeamDto(
                        child.child("name").value as String?,
                        child.child("pokemon").value as List<String>,
                        key = child.key
                    )
                    teams.add(team)
                }
                populatePokemonData(teams)
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().message?.let { Log.d("Application", it) }
            }
        }
        userTeamsRef.addValueEventListener(postListener)
    }

    private fun populatePokemonData(data: List<TeamDto?>){
        pkmnRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val teams = ArrayList<TeamDto?>()
                data.forEach { team ->
                    val pkmnList = ArrayList<PokemonDto>()
                    team?.pokemon?.forEach { pkmnKey ->
                        val result = snapshot.children.find { (it.key as String).equals(pkmnKey) }?.value as HashMap<*, *>
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
                        pkmnList.add(pkmnDto)
                    }
                    team?.pokemonList = pkmnList
                    teams.add(team)
                }

                presenter.receiveTeams(teams)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun saveTeamRdb(team:TeamDto) {
        TODO("Not yet implemented")
    }

    override fun deleteTeamRdb(team: TeamDto) {
        val shared = ctx.applicationContext.getSharedPreferences("pref", 0)
        val userKey = shared.getString("currentUser","")
        val userTeamsRef = usersRef.child(userKey!!).child("teams")
        team.key?.let { userTeamsRef.child(it).removeValue() }
    }

}