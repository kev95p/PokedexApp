package com.kev95p.pruebakotlin.data.firebasedb

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kev95p.pruebakotlin.data.dto.TeamDto

class RDatabase {
    private val database = FirebaseDatabase.getInstance()
    fun getAllTeams(){
        val myRef = database.getReference("teams")
        var postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val teams = ArrayList<TeamDto?>()
                snapshot.children.forEach { child->
                    val team: TeamDto? = TeamDto(
                        child.child("name").value as String?,
                        child.child("pokemon").value as List<String>?,
                        child.key
                    )
                    teams.add(team)
                }
                println(teams.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().message?.let { Log.d("Application", it) }
            }
        }
        myRef.addListenerForSingleValueEvent(postListener)
    }
}